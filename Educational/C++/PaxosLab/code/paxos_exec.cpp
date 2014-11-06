// Basic routines for Paxos implementation

#include "make_unique.h"
#include "paxmsg.h"
#include "paxserver.h"
#include "log.h"

void paxserver::execute_arg(const struct execute_arg& ex_arg) {

    /* inform client about new view-id */
    if (ex_arg.vid != vc_state.view.vid) {
        send_msg(ex_arg.nid,
                std::make_unique<struct execute_fail>(
                        vc_state.view.vid,
                        vc_state.view.primary,
                        ex_arg.rid));
        return; // don't log this message
    }

    if (paxlog.find_rid(ex_arg.src, ex_arg.rid)) {
        LOG(l::DEBUG, "duplicate request ?? ignoring...\n");
        return;
    }

    const struct viewstamp_t proposed_vs = { ex_arg.vid, ts++ };

    /* The primary cohort logs the request and forwards it to all other cohorts. */
    paxlog.log(
            ex_arg.nid,
            ex_arg.rid,
            proposed_vs,
            ex_arg.request,
            (uint)vc_state.view.get_servers().size(),
            ex_arg.sent_tick);

    paxlog.set_latest_accept(proposed_vs);

    for (auto backup : vc_state.view.backups) {
        LOG(l::DEBUG, "Sending " << proposed_vs << " to: " << backup << "\n");
        send_msg(backup,
                std::make_unique<struct replicate_arg>(
                        proposed_vs, ex_arg, paxlog.latest_exec()));
    }
}

void paxserver::replicate_arg(const struct replicate_arg& repl_arg) {
    /* replicate_arg:
           viewstamp_t  vs
           execute_arg  arg
           viewstamp_t  committed */

    /* this never happens */
    if (paxlog.find_rid(repl_arg.arg.src, repl_arg.arg.rid)) {
        LOG(l::DEBUG, "Dropping duplicate request.\n");
        return;
    }

    /* "Our backups log all requests, and acknowledge the primary after logging." */
    paxlog.log(
            repl_arg.arg.nid,
            repl_arg.arg.rid,
            repl_arg.vs,
            repl_arg.arg.request,
            (uint)vc_state.view.get_servers().size(),
            repl_arg.arg.sent_tick);

    /* this internally checks to make sure that the
        incoming vs is newer than the existing latest */
    paxlog.set_latest_accept(repl_arg.vs);

    /* Backups execute all requests less than or equal to the committed field in replicate_arg. */

    std::vector<std::unique_ptr<Paxlog::tup>>::iterator it;

    for (it = paxlog.begin(); it != paxlog.end() && (*it)->vs <= repl_arg.committed; it++) {
        viewstamp_t nil_vs = {};
        if (paxlog.next_to_exec(it) || paxlog.latest_exec() == nil_vs) {
            LOG(l::DEBUG, "executing backloged vs = " << (*it)->vs << "\n");
            paxop_on_paxobj(*it); // here we execute (to change the state of the state machine)
            paxlog.execute(*it);  // here we note that we executed
        }
    }

    /* The backup acknowledges the request by viewstamp */
    send_msg(vc_state.view.primary, std::make_unique<struct replicate_res>(repl_arg.vs));

    /* TODO "cohorts can safely delete log entries before committed" */
}

void paxserver::replicate_res(const struct replicate_res& repl_res) {
    /* replicate_res:
     *     const char* _descr = "replicaRES"
     *     viewstamp_t vs */

    /*************************************
        Meanwhile, back at the PRIMARY...
     *************************************/

    /* after receiving acknowledgments from a majority of cohorts (including itself),
       the primary calls the execute function on request and sends the reply back to the client */

    /* count this response */
    if (!paxlog.incr_resp(repl_res.vs)) {
        LOG(l::DEBUG, "couldn't find " << repl_res.vs << " in log, just send em another accept arg\n");
        send_msg(repl_res.src, std::make_unique<struct accept_arg>(paxlog.latest_exec()));
        return;
    }

    viewstamp_t nil_vs = {};
    Paxlog::tup const *this_tup = paxlog.get_tup(repl_res.vs);

    if (this_tup->resp_cnt == this_tup->serv_cnt) {
        LOG(l::DEBUG, "Request " << this_tup->vs << " has been ACK'd by everyone.\n");
    }

    /* if it has *just now* been Ack'd by a majority */
    else if (this_tup->resp_cnt == this_tup->serv_cnt/2 + 1) {
        LOG(l::DEBUG, "Primary just received enough votes for " << this_tup->vs << "\n");
        /* go through the log again from the start */
        for (auto it = paxlog.begin(); it != paxlog.end(); it++) {
            std::unique_ptr<Paxlog::tup> &tup = *it;

            LOG(l::DEBUG, "Primary may execute " << tup->vs << "\n");

            /* determine if this entry ought to be executed */

            /* already was executed, move on */
            if (tup->vs <= paxlog.latest_exec()) {
                LOG(l::DEBUG, "Primary says: " << tup->vs << " was already executed.\n");
                continue;
            }

            bool has_enough_votes   = tup->resp_cnt > tup->serv_cnt/2;
            bool none_have_execd    = paxlog.latest_exec() == nil_vs;
            bool first_entry        = it == paxlog.begin();
            bool cold_start         = none_have_execd && first_entry;
            bool next_to_exec       = paxlog.next_to_exec(it) || cold_start;
            bool should_execute     = has_enough_votes && next_to_exec;

            LOG(l::DEBUG, "has_enough_votes: " << has_enough_votes << "\n");
            LOG(l::DEBUG, "none_have_execd: "  << none_have_execd  << "\n");
            LOG(l::DEBUG, "first_entry: "      << first_entry      << "\n");
            LOG(l::DEBUG, "cold_start: "       << cold_start       << "\n");
            LOG(l::DEBUG, "next_to_exec: "     << next_to_exec     << "\n");
            LOG(l::DEBUG, "should_execute: "   << should_execute   << "\n");

            if (should_execute) {
                LOG(l::DEBUG, "Executing it.\n");
                std::string resp = paxop_on_paxobj(tup);
                paxlog.execute(tup);

                LOG(l::DEBUG, "Sending result " << resp << " to Client: " << tup->src << "\n");
                send_msg(tup->src, std::make_unique<struct execute_success>(resp, tup->rid));
            }
            else if (paxlog.latest_exec() != nil_vs && !paxlog.next_to_exec(it)) {
                LOG(l::DEBUG, "It wasn't next to execute\n");
                break;
            } else {
                LOG(l::DEBUG, tup->vs << "third condition\n");
                break;
            }
        }

        /* When the primary has no unexecuted entries in the Paxos log,
                propagate latest_accept using accept_arg. */
        if (paxlog.latest_exec() == paxlog.latest_accept()) {

            LOG(l::DEBUG, "The Primary's entire log has been executed.\n");

            /* trim the log */

            std::function<bool (const std::unique_ptr<Paxlog::tup>&)> trim_fctn =
                    [&](const std::unique_ptr<Paxlog::tup>& tptr) {
                        return /*tptr->vs <= paxlog.latest_exec()
                                && tptr->resp_cnt == tptr->serv_cnt*/true;};

            paxlog.trim_front(trim_fctn); // just clear the entire log

            LOG(l::DEBUG, "The Primary's log was trimmed ");
            if(!paxlog.empty()) {
                LOG(l::DEBUG, "but still isn't empty.\n");
            } else {
                LOG(l::DEBUG, "and is now empty.\n");
            }


            paxlog.set_latest_accept(nil_vs);

            /* tell the backups that this thing was committed
               so they can commit up to here and (potentially) trim too */
            for (auto backup : vc_state.view.backups) {
                send_msg(backup, std::make_unique<struct accept_arg>(paxlog.latest_exec()));
            }
        }
    }
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {
    /* accept_arg:
     *     const char* _descr = "accept ARG"
     *     viewstamp_t committed
     */

    viewstamp_t nil_vs = {};

     /* when the primary's log is empty it sends a message to the backups to accept <= committed */
    for (auto it = paxlog.begin(); it != paxlog.end() && (*it)->vs <= acc_arg.committed; it++) {
        std::unique_ptr<Paxlog::tup> &tup = *it;

        /* determine if this entry ought to be executed */
        bool none_have_execd    = paxlog.latest_exec() == nil_vs;
        bool first_entry        = it == paxlog.begin();
        bool cold_start         = none_have_execd && first_entry;
        bool next_to_exec       = paxlog.next_to_exec(it) || cold_start;

        LOG(l::DEBUG, "\n"                 << *tup            << "\n");
        LOG(l::DEBUG, "none_have_execd: "  << none_have_execd << "\n");
        LOG(l::DEBUG, "first_entry: "      << first_entry     << "\n");
        LOG(l::DEBUG, "cold_start: "       << cold_start      << "\n");
        LOG(l::DEBUG, "next_to_exec: "     << next_to_exec    << "\n");
//
        if (next_to_exec) {
            LOG(l::DEBUG, "backup " << nid << " is executing " << (*it)->vs << "\n");
            paxop_on_paxobj(*it);
            paxlog.execute(*it);  // here we note that we executed
        }
    }

    /* this seems like another good opportunity to trim the log */
    std::function<bool (const std::unique_ptr<Paxlog::tup>&)> trim_fctn =
            [&](const std::unique_ptr<Paxlog::tup>& tptr) {
                return tptr->vs <= acc_arg.committed
                    && tptr->vs <= paxlog.latest_exec();};

    paxlog.trim_front(trim_fctn);

    if (paxlog.empty()) {
        LOG(l::DEBUG, "Backup log now empty\n");
    }
    else {
        LOG(l::DEBUG, "Backup log still not empty\n");
    }
}
