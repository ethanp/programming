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

    /* duplicate request means the network timed out before responding to the client.
     * so we resend it out */
    if (paxlog.find_rid(ex_arg.src, ex_arg.rid)) {
        for (auto it = paxlog.begin(); it != paxlog.end(); it++) {
            if ((*it)->src == ex_arg.src && (*it)->rid == ex_arg.rid) {
                for (auto backup : vc_state.view.backups) {
                    LOG(l::DEBUG, "RESending " << (*it)->vs << " to: " << backup << "\n");
                    send_msg(backup,
                            std::make_unique<struct replicate_arg>(
                                    (*it)->vs, ex_arg, paxlog.latest_exec()));
                }
                return;
            }
        }
        MASSERT(0, "Shouldn't be here\n");
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

    /* "Our backups log all requests, and acknowledge the primary after logging." */
    if (!paxlog.find_rid(repl_arg.arg.src, repl_arg.arg.rid)) {
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
    }


    /* Backups execute all requests less than or equal to the committed field in replicate_arg. */

    std::vector<std::unique_ptr<Paxlog::tup>>::iterator it;

    for (it = paxlog.begin(); it != paxlog.end() && (*it)->vs <= repl_arg.committed; it++) {
        viewstamp_t nil_vs = {};
        bool cold_start = paxlog.latest_exec() == nil_vs && it == paxlog.begin();
        if (paxlog.next_to_exec(it) || cold_start) {
            LOG(l::DEBUG, "executing backlogged vs = " << (*it)->vs << "\n");
            paxop_on_paxobj(*it); // here we execute (to change the state of the state machine)
            paxlog.execute(*it);  // here we note that we executed
        }
    }

    /* The backup acknowledges the request by viewstamp */
    send_msg(vc_state.view.primary, std::make_unique<struct replicate_res>(repl_arg.vs));
}

void paxserver::replicate_res(const struct replicate_res& repl_res) {

    /* after receiving acknowledgments from a majority of cohorts (including itself),
       the primary calls the execute function on request and sends the reply back to the client */

    /* count this response */
    if (!paxlog.incr_resp(repl_res.vs)) {
        /* someone who hadn't acked yet, send out another accept */
        send_msg(repl_res.src, std::make_unique<struct accept_arg>(paxlog.latest_exec()));
        return;
    }

    viewstamp_t nil_vs = {};
    Paxlog::tup const *this_tup = paxlog.get_tup(repl_res.vs);

    /* if it has *just now* been Ack'd by a majority */
    if (this_tup->resp_cnt == this_tup->serv_cnt/2 + 1) {
        LOG(l::DEBUG, "Primary just received enough votes for " << this_tup->vs << "\n");
        for (auto it = paxlog.begin(); it != paxlog.end(); it++) {
            std::unique_ptr<Paxlog::tup> &tup = *it;

            /* already was executed, move on */
            if (tup->vs <= paxlog.latest_exec()) {
                continue;
            }

            bool has_enough_votes   = tup->resp_cnt > tup->serv_cnt/2;
            bool none_have_execd    = paxlog.latest_exec() == nil_vs;
            bool first_entry        = it == paxlog.begin();
            bool cold_start         = none_have_execd && first_entry;
            bool next_to_exec       = paxlog.next_to_exec(it) || cold_start;
            bool should_execute     = has_enough_votes && next_to_exec;

            if (should_execute) {
                std::string resp = paxop_on_paxobj(tup);
                paxlog.execute(tup);

                LOG(l::DEBUG, "Sending result " << resp << " to Client: " << tup->src << "\n");
                send_msg(tup->src, std::make_unique<struct execute_success>(resp, tup->rid));
            }
            else {
                break;
            }
        }

        /* When the primary has no unexecuted entries in the Paxos log,
                propagate latest_accept using accept_arg. */
        if (paxlog.latest_exec() == paxlog.latest_accept()) {

            LOG(l::DEBUG, "The Primary's entire log has been executed.\n");

            /* clear the log */
            paxlog.trim_front([&](const std::unique_ptr<Paxlog::tup>& tptr){return true;});

            LOG(l::DEBUG, "The Primary's log was trimmed\n");

            paxlog.set_latest_accept(nil_vs);

            /* tell the backups that this thing was committed
               so they can commit up to here and (potentially) trim too */
            for (auto backup : vc_state.view.backups) {
                LOG(l::DEBUG, "sending " << backup << " accept\n");
                send_msg(backup, std::make_unique<struct accept_arg>(paxlog.latest_exec()));
            }
        }
    }
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {

    LOG(l::DEBUG, "still alive!\n");

    /* when the primary's log is empty it sends a message to the backups to accept <= committed */
    if (paxlog.empty()) {
        return;
    }
    viewstamp_t nil_vs = {};
    for (auto it = paxlog.begin(); it != paxlog.end() && (*it)->vs <= acc_arg.committed; it++) {

        std::unique_ptr<Paxlog::tup> &tup = *it;
        bool cold_start = paxlog.latest_exec() == nil_vs && it == paxlog.begin();
        if (paxlog.next_to_exec(it) || cold_start) {
            LOG(l::DEBUG, "backup " << nid << " is executing " << tup->vs << "\n");
            paxop_on_paxobj(tup);
            paxlog.execute(tup);
        }
    }

    /* this seems like another good opportunity to trim the log */
    std::function<bool(const std::unique_ptr<Paxlog::tup> &)> trim_fctn =
            [&](const std::unique_ptr<Paxlog::tup> &tptr) {
                return tptr->vs <= acc_arg.committed
                        && tptr->vs <= paxlog.latest_exec();
            };

    if (!paxlog.empty()) {
        paxlog.trim_front(trim_fctn);
    }

    if (paxlog.empty()) {
        LOG(l::DEBUG, "Backup log now empty\n");
        paxlog.set_latest_accept(nil_vs);
    }
    else {
        LOG(l::DEBUG, "Backup log still not empty\n");
    }
}
