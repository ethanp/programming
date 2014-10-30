// Basic routines for Paxos implementation

#include "make_unique.h"
#include "paxserver.h"
#include "log.h"

//////////////////////////////////////////////////////
// paxserver

// Static data item, inter-server normal messages
std::unordered_set<int> paxserver::normal_msg = {
   execute_arg::ID,
   replicate_arg::ID,
   replicate_res::ID,
   accept_arg::ID,
};
  
paxserver::paxserver(Net* _net, node_id_t _nid, const pax_serv_timo& _ps_timo,
                     std::unique_ptr<paxobj> mypaxobj) :
   node_t(_net, _nid)
{
   vc_state = {}; // C++ better than memset
   // When we start, we need to view change
   vc_state.mode = vc_state_t::UNDERLING;
   MASSERT(vc_state.view.vid.counter == 0ULL, "expect");
   MASSERT(vc_state.view.vid.manager == (node_id_t)0, "expect");
   vc_mgr = {};
   // Timestamps start at 1, not 0
   ts = 1ULL;
   _paxobj = std::move(mypaxobj);
   ps_timo = _ps_timo;
   stat = {};
   sprintf(my_id, "S%02d", nid);
}
paxserver::~paxserver() {
   _paxobj = nullptr;
   last_req.clear();
   exec_rid_cache.clear();
}

bool paxserver::send_msg(node_id_t dst, std::unique_ptr<net_msg_t> msg) {
   bool res = net->send(this, dst, std::move(msg));
   // Don't check result. Might be sending to dead node
   recent_send[dst] = net->now();
   return res;
}

unsigned int paxserver::get_serv_cnt(const view_t& view) {
   return (unsigned int)(view.backups.size() + 1);
}

// NB: This is not in paxos_exec, because that way we can distribute
// it as-is.
std::string paxserver::paxop_on_paxobj(std::unique_ptr<Paxlog::tup>& logop) {
   auto result = _paxobj->execute(std::move(logop->request));
   exec_rid_cache.insert({logop->src, logop->rid});
   // Erase oldest entries (at end) until we fall below max_rid_in_cache
   // XXX Is this compiler dependent?  STL defines it, but is that true in practice?
   while(exec_rid_cache.count(logop->src) > max_rid_in_cache) {
      auto range = exec_rid_cache.equal_range(logop->src);
      std::advance(range.first, max_rid_in_cache);
      exec_rid_cache.erase(range.first);
   }
   last_req[logop->src] = std::make_unique<last_tup_res>();
   LOG(l::DEBUG, *logop << '\n');
   // Note, logop->request is now nullptr
   last_req[logop->src]->tup = std::make_unique<Paxlog::tup>(*logop);
   last_req[logop->src]->result = result;
   return result;
}

// Primarily our action depends on incoming message type, but
// drop all normal requests (execute, replicate, accept) during view
// change 
void paxserver::dispatch(paxmsg_t &paxmsg) {
   net->rpc_incr(paxmsg.rpc_id);
   // Set basis for timeout
   if(paxmsg.rpc_id != execute_arg::ID) {
      if(paxmsg.sent_tick > recent_recv[paxmsg.src]) {
         // Note that I rely on reasonably synchronized clocks here, by checking
         // a tick count done on a different machine
         recent_recv[paxmsg.src] = paxmsg.sent_tick;
      }
   }
   if(vc_state.mode != vc_state_t::ACTIVE
      && is_normal_msg(paxmsg.rpc_id)) {
      net->drop(this, paxmsg, "not ACTIVE");
      return;
   }
   switch(paxmsg.rpc_id) {
   case nop_msg::ID: 
      break;
   case execute_arg::ID:
      execute_arg(static_cast<const struct execute_arg&>(paxmsg));
      break;
   case replicate_arg::ID:
      replicate_arg(static_cast<const struct replicate_arg&>(paxmsg));
      break;
   case replicate_res::ID:
      replicate_res(static_cast<const struct replicate_res&>(paxmsg));
      break;
   case accept_arg::ID :
      accept_arg(static_cast<const struct accept_arg&>(paxmsg));
      break;
   case view_change_arg::ID:
      view_change_arg(static_cast<const struct view_change_arg&>(paxmsg));
      break;
   case view_change_reject::ID:
      view_change_reject(
         static_cast<const struct view_change_reject&>(paxmsg));
      break;
   case view_change_accept::ID:
      view_change_accept(
         static_cast<const struct view_change_accept&>(paxmsg));
      break;
   case new_view_arg::ID:
      new_view_arg(static_cast<const struct new_view_arg&>(paxmsg));
      break;
   case new_view_res::ID:
      new_view_res(static_cast<const struct new_view_res&>(paxmsg));
      break;
   case init_view_request::ID:
      init_view_request(
         static_cast<const struct init_view_request&>(paxmsg));
      break;
   case init_view_arg::ID:
      init_view_arg(static_cast<const struct init_view_arg&>(paxmsg));
      break;
   case getstate_arg::ID:
      getstate_arg(static_cast<const struct getstate_arg&>(paxmsg));
      break;
   case getstate_res::ID:
      getstate_res(static_cast<struct getstate_res&>(paxmsg));
      break;
   default:
      MASSERT(0, "%s %d Should be a handler for each RPC", 
              id_str(), paxmsg.rpc_id);
   }
}

void paxserver::do_heartbeat() {
   switch(vc_state.mode) {
   case vc_state_t::ACTIVE: {
      // Primary heartbeats servers in view.  No one has to heartbeat the 
      // primary, since if we don't hear from it, we will vc
      if(primary()) {
         std::set<node_id_t> servers = get_other_servers(vc_state.view);
         for(const auto& serv : servers) {
            if(net->timop(recent_send[serv], ps_timo.heartbeat_timo)) {
               LOG(l::DBG_EV, id_str() << " pr heartbeat now:" << net->now()
                   << " recent send: " << recent_send[serv]
                   << " nid:" << serv << "\n");
               send_msg(serv, std::make_unique<nop_msg>());
            }
         }
      }
   }
      break;
   case  vc_state_t::MANAGER: {
      // If I'm the manager, heartbeat all servers
      // Underlings don't need to heartbeat, they will timeout on manager
      std::set<node_id_t> servers = net->get_serv_ids(nid);
      for(const auto& serv : servers) {
         // Send heartbeat to any server in our view that 
         // we haven't sent a message to in a while
         if(net->timop(recent_send[serv], ps_timo.heartbeat_timo)) {
            LOG(l::DBG_EV, id_str() << " heartbeat now:" << net->now()
                << " recent send: " << recent_send[serv]
                << " nid:" << serv << "\n");
            send_msg(serv, std::make_unique<nop_msg>());
         }
      }
   }
      break;
   case  vc_state_t::UNDERLING:
      // No need, just timeout if manager doesn't contact us
      break;
   }
}

// NB: For each timeout in struct pax_serv_timo, there should
// be code here to process the timeout
void paxserver::do_timo() {
   // If we just woke up, chill on initiating vc (routes to which are below)
   // Because we just sent nops, which should induce vc if we aren't part of the view
   // XXX hardcoded constant
   if((net->now() - unpaused_tick) < 15) {
      return;
   }

   switch(vc_state.mode) {
   case vc_state_t::ACTIVE: {
      std::set<node_id_t> servers = get_other_servers(vc_state.view);
      if(primary()) {
         // First check if we have heard from someone not in our view with
         // a packet sent "recently" (i.e., around half of heartbeat_timo).
         // If so, view change.
         for(auto rr_it = recent_recv.begin(); rr_it != recent_recv.end(); ++rr_it) {
            if(servers.count(rr_it->first) == 0
               // XXX hardcoded constant
               && (net->now() - rr_it->second) < 40) {
               // Recently received a message from an out of view host, vc
               LOG(l::DBG_EV, id_str() << " out of view now:" << net->now()
                   << " host: " << rr_it->first
                   << " gotit:" << rr_it->second << "\n");
               rr_it->second = 0;
               initiate_vc(true);
               return;
            }
         }
         
         // Now check to see we have heard from everyone in view
         for(const auto& serv : servers) {
            if(net->timop(recent_recv[serv], ps_timo.dead_timo)) {
               LOG(l::DBG_EV, id_str() << " active timeout now:" << net->now()
                   << " recent recv: " << recent_recv[serv]
                   << " nid:" << serv << "\n");
               initiate_vc(true);
               return;
            }
         }
      } else {
         // Everyone else checks primary
         if(net->timop(recent_recv[vc_state.view.primary],
                       ps_timo.dead_timo)) {
            // If we haven't heard from the manager, initiate_vc
            LOG(l::DBG_EV, id_str() << " non-primary timeout now:" << net->now()
                << " recent recv: " << recent_recv[vc_state.view.primary] 
                << " pr: " << vc_state.view.primary << "\n");
            initiate_vc(true);
            return;
         }
      }
   }
      break;
   case  vc_state_t::MANAGER: {
      // If we haven't announced new view, & it is possible
      // see if we have waited long enough
      if(vc_mgr.announce_nv == false
         && vc_mgr.view_possible 
         && net->timop(vc_mgr.view_possible, ps_timo.vca_timo)) {
         LOG(l::DBG_EV, id_str() << " manager vc complete timeout now:" 
             << net->now() << "\n");
         form_newview();
         announce_newview();
         vc_mgr.announce_nv = true;
      }
      MASSERT(ps_timo.vca_timo < ps_timo.dead_timo,
              "Should wait longer for dead node than to complete a vc");
      // Now see if we haven't heard from anyone in too long a time.
      std::set<node_id_t> servers = net->get_serv_ids(nid);
      tick_t most_recent_recv = (tick_t)0;
      node_id_t recent_nid = 0;
      for(const auto& serv : servers) {
         if(recent_recv[serv] > most_recent_recv) {
            most_recent_recv = recent_recv[serv];
            recent_nid = serv;
         }
      }
      if(net->timop(most_recent_recv, ps_timo.dead_timo)) {
         LOG(l::DBG_EV, id_str() << " manager timeout now:" << net->now()
             << " most recent: " << most_recent_recv
             << " nid:" << recent_nid << "\n");
         initiate_vc(true);
         return;
      }
   }
      break;
   case vc_state_t::UNDERLING: {
      if(net->timop(recent_recv[vc_state.proposed_vid.manager], 
                    ps_timo.dead_timo)) {
         // If we don't hear from the manager, initiate_vc
         LOG(l::DBG_EV, id_str() << " underling timeout now:" << net->now()
             << " recent_recv: " << recent_recv[vc_state.proposed_vid.manager]
             << " nid:" << vc_state.proposed_vid.manager << "\n");
         initiate_vc(true);
      }
   }
      break;
   default:
      MASSERT(0, "Yikes");
   }
}

void paxserver::do_init_vc() {
   // I'm a new born babe, consider myself primary
   // get_serv_ids not actually more powerful than
   // get_rand_server, and we need something (e.g., broadcast RPC)
   // to figure
   // out who to build a view with.  This code should work with
   // just get_rand_server instead of get_all_serv_ids
   std::set<node_id_t> serv_ids = net->get_serv_ids(nid);
   MASSERT(serv_ids.size() >= 2,
           "For fault tolerance, paxos generally runs"
           "at least 3 servers");
   vc_state.view.primary = nid;
   vc_state.view.backups = serv_ids;
   initiate_vc(true);
}

void paxserver::do_fake_init_vc() {
   node_id_t pr = nid;
   view_t v;
   v.backups.insert(pr);
   for (const auto id : net->get_serv_ids(nid)) {
      v.backups.insert((node_id_t)id);
      if (pr < id) {
         pr = id;
      }
   }
   v.backups.erase(pr);
   MASSERT(v.backups.size() >= 2,
           "For fault tolerance, paxos generally runs"
           "at least 3 servers");
   v.primary = pr;
   v.vid.counter = 1;
   v.vid.manager = pr;

   vc_state.mode = vc_state_t::ACTIVE;
   vc_mgr = {};
   vc_state.view = v;
   vc_state.accepted_view = nullptr;
   if (nid == pr) {
      ts = 1ULL;
      viewstamp_t last_vs_old_view  = paxlog.latest_exec();
      vc_state.latest_seen = last_vs_old_view;
      vc_state.proposed_vid = v.vid;
      LOG(l::DBG_VC, "Initial view " << v << "\n");
   } else {
      viewstamp_t new_vs;
      new_vs.vid = vc_state.view.vid;
      new_vs.ts = 0ULL;
      paxlog.set_latest_exec(new_vs);
   }
}

bool paxserver::tick(void) {
   if( vc_state.view.primary == (node_id_t)0 ) {
      if (net->use_fake_init_vc() && net->get_initial_num_servers() >= nid)
         do_fake_init_vc();
      else
         do_init_vc();
      return false;
   }
   // Processing messages is much faster than transmitting them,
   // So let's process 3 messages per tick
   if(!net->delay()) {
      for(int i = 0; i < 3; ++i) {
         auto nm = net->recv(this);
         if( nm ) {
            dispatch(static_cast<paxmsg_t&>(*nm)); // process message
         }
      }
   }
   do_timo();

   // What keeps a simulation alive:
   //  1. Client work 2. Messages in the network 3. Unprocessed log entries on backups
   if(vc_state.mode == vc_state_t::ACTIVE
      && (vc_state.view.primary != nid)
      && !paxlog.empty()) {
      return true;
   }
   return false;
}

/////////////////////////////////////////////////////////////
// Functions defined in node.h, not for public (or even internal) consumption
bool paxserver::primary() const {
   return (vc_state.mode == vc_state_t::ACTIVE)
      &&(vc_state.view.primary == nid);
}

bool paxserver::in_view(node_id_t node) const {
   MASSERT(primary(), "Only query in_view of primary");
   return vc_state.view.elt_in(node);
}   

// Limited lifetime, provided mostly for stats or checking equality.
paxobj*  paxserver::get_paxobj() { 
   return _paxobj.get();
}

