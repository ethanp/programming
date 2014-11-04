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
 *         node_id_t src;
 *         uint64_t pkt_num;  // System-wide unique packet number, for debugging, written in Net::send
 *         tick_t sent_tick;  // Tick packet was sent, written in Net::send.
 *         const char* descr; // Text description of the message 10 chars exactly
 */
void paxserver::execute_arg(const struct execute_arg& ex_arg) {
    /* execute_arg:
     *     node_id_t nid,
     *     rid_t     rid,
     *     viewid_t  vid,
     *     paxobj::request request == shared_ptr<op>:
     *         std::function<std::string (paxobj*)> func
     *         const std::string& name
     */
    /* 1. This is the initial step of the request being received by the primary.
     * 2. The point of this step is to put replicate args CONTAINING this
     *      execute_arg on the queues of all the backup cohorts.
     */
    if (ex_arg.vid < vc_state.view.vid) {
        /* TODO Do I need to ensure that ex_arg._vid == vc_state.view.vid ? */
        LOG(l::DEBUG, "ex_arg.vid < vc_state.view.vid" << "\n");
    }

    /* TODO is this correct? */
    const struct viewstamp_t proposed_vs = { vc_state.view.vid, ts };
    paxlog.set_latest_accept(proposed_vs);

    for (auto backup : vc_state.view.backups) {
        send_msg(backup,
                std::make_unique<struct replicate_arg>(
                        paxlog.latest_accept(), ex_arg, paxlog.latest_exec()));
    }
}

void paxserver::replicate_arg(const struct replicate_arg& repl_arg) {
    /* replicate_arg:
     *     viewstamp_t vs
     *     execute_arg arg
     *     viewstamp_t committed
     */

    /* "Our backups log all requests, and acknowledge the primary after logging." */
    // So he's telling me to log the request now.
    paxlog.log(
            repl_arg.arg.nid,
            repl_arg.arg.rid,
            repl_arg.vs,
            repl_arg.arg.request,
            (unsigned int)vc_state.view.get_servers().size(), // I find this odd.
            repl_arg.arg.sent_tick);


    /* But backups and the primary execute operations in strict viewstamp order (see viewstamp_t::sucessor).
       Backups execute all requests less than or equal to the committed field in replicate_arg.
       The quite ugly iterator interface to Paxlog (Paxlog::begin and Paxlog::end) are
       provided for you to traverse the log to determine which entries can be executed. */

    // we iterate through this backup's log, executing entries that have been committed by the primary
    std::vector<std::unique_ptr<Paxlog::tup>>::iterator it;
    for (it = paxlog.begin(); it != paxlog.end() && (*it)->vs < repl_arg.committed; it++) {
        if (paxlog.next_to_exec(it)) {

            /* TODO am I doing this right? */
            paxop_on_paxobj(*it); // here we execute
            paxlog.execute(*it); //  here we note that we executed
        }
    }
    /* Respond to the primary with TODO what/which viewstamp_t (this one is filler) ? */
    send_msg(vc_state.view.primary, std::make_unique<struct replicate_res>(paxlog.latest_accept()));
}

void paxserver::replicate_res(const struct replicate_res& repl_res) {
    /* replicate_res:
     *     const char* _descr = "replicaRES"
     *     viewstamp_t vs
     */
    /* 1. This is, the primary responding to the responses from the backups to its proposal.
     * 2. It must use the incoming `vs` to decide what to do about the client's proposal.
     * 3. Whether it decides to tell everyone to commit the proposal or not,
     *      it will respond to the client about what happened through an `accept_arg` (I guess).
     * 4. I suppose it also tells all the backups to go through with the commit?
     *          I'm not sure when all that takes place....
     */
    // TODO is this when we actually *execute* the execute_arg?
   MASSERT(0, "replicate_res not implemented\n");
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {
    /* accept_arg:
     *     const char* _descr = "accept ARG"
     *     viewstamp_t committed
     */
   MASSERT(0, "accept_arg not implemented\n");
}
