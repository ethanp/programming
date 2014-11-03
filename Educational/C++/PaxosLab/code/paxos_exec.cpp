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
     *     node_id_t _nid,
     *     rid_t _rid,
     *     viewid_t _vid,
     *     paxobj::request _request == shared_ptr<op>:
     *         std::function<std::string (paxobj*)> _func
     *         const std::string& _name
     */
    /* 1. This is the initial step of the request being received by the primary.
     * 2. The point of this step is to put replicate args CONTAINING this
     *      execute_arg on the queues of all the backup cohorts.
     */
   MASSERT(0, "execute_arg not implemented\n");
}

void paxserver::replicate_arg(const struct replicate_arg& repl_arg) {
    /* replicate_arg:
     *     viewstamp_t vs
     *     execute_arg arg
     *     viewstamp_t committed
     */
    /* 1. This is step two of the main protocol. Received by all the (online) backups.
     * 2. Here, we decide whether the `execute_arg` is OK to accept according to what
     *      we know of the state of the protocol, and we generate a `replicate_res`
     *      to put on the queue of the sender of this replicate_arg
     */
   MASSERT(0, "replicate_arg not implemented\n");
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
   MASSERT(0, "replicate_res not implemented\n");
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {
    /* accept_arg:
     *     const char* _descr = "accept ARG"
     *     viewstamp_t committed
     */
   MASSERT(0, "accept_arg not implemented\n");
}
