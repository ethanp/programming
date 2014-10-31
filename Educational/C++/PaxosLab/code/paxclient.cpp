#include <algorithm>
#include "paxclient.h"
#include "log.h"

// req_rec is the client analogy to a Paxlog::tup in the server
paxclient::req_rec::req_rec(paxclient* _client, rid_t _rid, 
                            const viewid_t& _vid,
                            paxobj::request _request, cb _reply_cb) :
   client(_client),
   timo_cnt(0),
   rid(_rid),
   vid(_vid),
   request(std::move(_request)),
   reply_cb(std::move(_reply_cb)) 
{
   tick_last_sent = client->net->now();
}

std::ostream& paxclient::req_rec::pr(std::ostream& os) const {
   os << "rid:" << rid << " vid:" << vid << " req:" << request->name;
   return os;
}

bool paxclient::req_rec::check_timo() {
   if(client->net->timop(tick_last_sent, client->timo)) {
      timo_cnt++;
      return true;
   }
   return false;
}

void paxclient::req_rec::reset_timo() {
   tick_last_sent = client->net->now();
}

//////////////////////////////////////////////////////
// paxclient
paxclient::paxclient(Net* _net, node_id_t _nid, int _timo,
                     int _switch_ntimo) :
   node_t(_net, _nid)
{
   timo = _timo;
   switch_ntimo = _switch_ntimo;
   // NB: Must initialize servers before clients
   primary = net->get_rand_server();
   MASSERT(primary != (node_id_t)0,
           "Must initialize servers before clients");
   // Start with illegal vid, so our first request will fail
   // and server will send a valid vid
   local_vid = {};
   local_rid = (rid_t)1; // Let's keep 0 unused
   stat = {};
   sprintf(my_id, "C%02d", nid);
   LOG(l::DEBUG, id_str() << " primary:" << primary << "\n");
}
paxclient::~paxclient() {
}

std::shared_ptr<paxclient::req_rec>
paxclient::next_req_rec() {
   MASSERT(!work_done(), "If we are done, don't create more work");
   // NB: work_get calls LOG
   auto rcb = work_get();
   MASSERT(rcb->request,  "Need request");
   MASSERT(rcb->reply_cb, "Need reply cb");
   LOG(l::DEBUG, " rid:" << local_rid << "\n");
   return std::make_unique<req_rec>(this, local_rid++, local_vid, 
                                    std::move(rcb->request),
                                    std::move(rcb->reply_cb));
}

std::shared_ptr<paxclient::req_rec>
paxclient::find_kill_req_rec(rid_t rid) {
   auto reqit = std::find_if(out_req.cbegin(), out_req.cend(),
                             [=](const std::shared_ptr<req_rec>& rr) {
                                return rid == rr->rid;
                             });
   if(reqit == out_req.end()) {
      LOG(l::WARN, id_str() << " rid:" << rid << " not found, duplicate or prev vid?\n");
      return nullptr;
   }
   // A set won't let us modify its elements,
   // so copy it, then erase it
   std::shared_ptr<paxclient::req_rec> res = *reqit; 
   out_req.erase(reqit);
   return res;
}
void
paxclient::erase(rid_t rid) {
   auto reqit = std::find_if(out_req.begin(), out_req.end(),
                             [=](const std::shared_ptr<req_rec>& rr) {
                                return rid == rr->rid;
                             });
   MASSERT(reqit != out_req.end(),
           "Don't try to erase a non-existent request");
   out_req.erase(reqit);
}

void paxclient::execute_success(struct execute_success* ex_succ) {
   auto rr = find_kill_req_rec(ex_succ->rid);
   if(rr) {
      if(rr->reply_cb) {
         (*rr->reply_cb)(ex_succ->reply);
      } else {
         LOG(l::WARN, "Yikes, null cb ex_succ:" << *ex_succ << "\n");
      }
      LOG(l::DEBUG, id_str() << " ex_succ:" << *ex_succ
          << "\n   req_rec:" << (*rr) << "\n");
   }
}

void paxclient::execute_fail(struct execute_fail* ex_fail) {
   auto rr = find_kill_req_rec(ex_fail->rid);
   if(rr) {
      MASSERT(rr->request, "Null request!");
      LOG(l::DEBUG, id_str() << " ex_fail:" << *ex_fail
          << " req_rec:" << *rr << "\n");
      // The old request failed.
      // Update our view information and make a new request
      primary = ex_fail->primary;
      local_vid = ex_fail->vid;
      auto new_rr = std::make_unique<req_rec>(
         this, local_rid++, local_vid,
         rr->request,
         std::move(rr->reply_cb));
      auto new_ex_arg = std::make_unique<struct execute_arg>(
         nid, new_rr->rid, new_rr->vid, rr->request);
      net->send(this, primary, std::move(new_ex_arg));
      out_req.insert(std::move(new_rr));
      stat.started_op++;
   }
}

void paxclient::dispatch(paxmsg_t* paxmsg) {
   // Message handlers
   switch(paxmsg->rpc_id) {
      case execute_success::ID : {
         execute_success(static_cast<struct execute_success*>(paxmsg));
         stat.success_op++;
      }
         break;
      case execute_fail::ID : {
         execute_fail(static_cast<struct execute_fail*>(paxmsg));
      }
         break;
      default:
         MASSERT(0, "No client default on dispatch");
   }
}

void paxclient::do_timo() {
   for( const auto& rr : out_req ) {
      if(rr->check_timo()) {
         // If the primary crashes, we eventually have to try another
         // server or we deadlock the simulation.
         // Try another server with the same rid.  Because that should
         // succeed at most once in a given view (though I'm not 100%
         // sure my executed_rid_cache provides those semantics)
         if(rr->timo_cnt++ > switch_ntimo) {
            rr->timo_cnt = 0;
            node_id_t new_pr = net->get_rand_server();
            int tries = 0;
            while(primary == new_pr) {
               new_pr = net->get_rand_server();
               if(tries++ > 10000) {
                  LOG(l::WARN, 
                      "Yikes, 10,000 tries to get a different random server\n"
                      "Must only be one server left\n");
                  break;
               }
            }
            primary = new_pr;
            LOG(l::DEBUG, "   new pr:" << primary 
                << " vid:" << local_vid << "\n");
         }
         rr->reset_timo();
         auto new_ex_arg = std::make_unique<struct execute_arg>(
            nid, rr->rid, rr->vid, rr->request);
         LOG(l::DEBUG, id_str() << " rexmit pr:" << primary 
             << " tick:" << net->now() << " " << *new_ex_arg << "\n");
         net->send(this, primary, std::move(new_ex_arg));
      }
   }
}

bool paxclient::tick(void) {
   // Deal with any messages
   auto nm = std::move(net->recv(this));
   if( nm ) {
      // Get, not release because paxmsg is a paxmsg_t*
      // so nm going out of scope causes deletion
      dispatch(static_cast<paxmsg_t*>(nm.get()));
   }
   // We have processed all callbacks
   if(work_done()) {
      return false;
   } else {
      if(out_req.size() > 0) {
         // Dispatch before timeout because we might have just
         // gotten the packet we are waiting for
         do_timo();
      } else {
         // Only allow new request if we have 0 pending
         // Therefore only one active request at a time (for now)
         auto new_rr = next_req_rec();
         auto new_ex_arg = std::make_unique<struct execute_arg>(
            nid, new_rr->rid, new_rr->vid, new_rr->request);
         net->send(this, primary, std::move(new_ex_arg));
         out_req.insert(std::move(new_rr));
         stat.started_op++;
      }
   }
   return true;
}

