// Basic routines for Paxos implementation

#include "make_unique.h"
#include "paxmsg.h"
#include "paxserver.h"
#include "log.h"

/* the incoming structs to all of these functions have the following inherited members
 * from pax_msg_t:
 *     const char* _descr,
 *     int _rpc_id
 *     inherits net_msg_t:
 *         node_id_t sr
 *         uint64_t pkt_num  ---  System-wide unique packet number, for debugging, written in Net::send
 *         tick_t sent_tick  ---  Tick packet was sent, written in Net::send.
 *         const char* desc  ---  Text description of the message 10 chars exactly */
void paxserver::execute_arg(const struct execute_arg& ex_arg) {
    /* execute_arg:
     *     node_id_t nid
     *     rid_t     rid
     *     viewid_t  vid
     *     paxobj::request request == shared_ptr<op>:
     *         std::function<std::string (paxobj*)> func
     *         const std::string& name */

    /* "The primary cohort logs the request and forwards it to all other cohorts." */

    /* client needs update */
    if (ex_arg.vid < vc_state.view.vid) {
        LOG(l::DEBUG, "ex_arg.vid < vc_state.view.vid" << "\n");

        /* tell client what's good */
        send_msg(ex_arg.nid,
                std::make_unique<struct execute_fail>(
                        vc_state.view.vid,
                        vc_state.view.primary,
                        ex_arg.rid));

        return; // don't log this message
    }

    /* The primary server includes the viewstamp it assigns an
        operation when forwarding the operation to backups */
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
        send_msg(backup,
                std::make_unique<struct replicate_arg>(
                        proposed_vs, ex_arg, paxlog.latest_exec()));
    }
}

void paxserver::replicate_arg(const struct replicate_arg& repl_arg) {
    /* replicate_arg:
     *     viewstamp_t vs
     *     execute_arg arg
     *     viewstamp_t committed */

    /* "Our backups log all requests, and acknowledge the primary after logging." */
    paxlog.log(
            repl_arg.arg.nid,
            repl_arg.arg.rid,
            repl_arg.vs,
            repl_arg.arg.request,
            (uint)vc_state.view.get_servers().size(),
            repl_arg.arg.sent_tick);


    /* But backups and the primary execute operations in strict viewstamp order (see viewstamp_t::sucessor).
       Backups execute all requests less than or equal to the committed field in replicate_arg.
       The quite ugly iterator interface to Paxlog (Paxlog::begin and Paxlog::end) are
       provided for you to traverse the log to determine which entries can be executed. */

    // we iterate through this backup's log, executing entries that have been committed by the primary
    std::vector<std::unique_ptr<Paxlog::tup>>::iterator it;

    /* committed specifies a viewstamp below which the server has executed all requests
       and sent their results back to clients. These committed operations never need to
       be rolled back and can therefore be executed at backups. */
    for (it = paxlog.begin(); it != paxlog.end() && (*it)->vs < repl_arg.committed; it++) {
        if (paxlog.next_to_exec(it)) {

            paxop_on_paxobj(*it); // TODO here we execute (to change the state of the state machine)
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
        meanwhile, back at the PRIMARY...
     *************************************/

    /* after receiving acknowledgments from a majority of cohorts (including itself),
       the primary calls the execute function on request and sends the reply back to the client */

    /* we know we've hit a majority of cohorts when tup.resp_cnt > tup.serv_cnt / 2 */
    // TODO get tup, increment res_cont, check majority-condition.

    /* we cast-away the 'const' modifier, bc we need to edit it */
    Paxlog::tup* tup = (Paxlog::tup *) paxlog.get_tup(repl_res.vs);
    if (!tup) {
        LOG(l::DEBUG, "couldn't find tup in log, CANCELLING" << "\n");
        return;
    }
    if (tup && tup->vs == repl_res.vs) {
        // make sure it's been a majority
        if (++tup->resp_cnt > tup->serv_cnt/2) {
            // If it has, execute if it hasn't already been executed
            // Am I allowed to execute even if something behind it in the log hasn't executed?
            // I'd think not... but it's impossible to intuit these types of things.
            auto rickshaw = std::make_unique<Paxlog::tup>(tup);
            std::string resp = paxop_on_paxobj(rickshaw);
            paxlog.execute(rickshaw);

            // send result back to the client
            send_msg((tup)->src,
                    std::make_unique<struct execute_success>(
                            resp, tup->rid));

        }
    }

    /* judging by the next function, I guess we tell the backups
        that this thing was committed so they can commit up to
        here too */
    /* TODO actually this should only happen if the log is now empty */
    for (auto backup : vc_state.view.backups) {
        send_msg(backup, std::make_unique<struct accept_arg>(tup->vs));
    }
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {
    /* accept_arg:
     *     const char* _descr = "accept ARG"
     *     viewstamp_t committed
     */
     /* when the primary's log is empty it sends a message to the backups to accept <= committed */
    for (auto it = paxlog.begin(); it != paxlog.end() && (*it)->vs < acc_arg.committed; it++) {
        if (paxlog.next_to_exec(it)) {
            paxop_on_paxobj(*it);
            paxlog.execute(*it);  // here we note that we executed
        }
    }
}
