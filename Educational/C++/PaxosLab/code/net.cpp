#include <vector>
#include <string>
#include <exception>
#include <algorithm>
#include <iomanip>
#include <cstdio>

#include "net.h"
#include "dssim.h"
#include "make_unique.h"
#include "node.h"
#include "log.h"
#include "massert.h"

uint64_t net_msg_t::_pkt_num = 1ULL;

Net::Net(dssim_t* _dssim) :
   dssim(_dssim)
{
   stat = {};
}
Net::~Net() {
   inqs.clear();
}

node_id_t Net::get_new_nid() {
   // Don't allow nid 0
   static node_id_t _nid = 1;
   node_id_t nid = _nid++; // could be randomly chosen from large space
   return nid;
}

void Net::announce(node_id_t node) {
   inqs[node] = std::make_unique<std::deque<
                                    std::unique_ptr<net_msg_t>>>();
}
void Net::die(node_id_t node) {
   LOG(l::DEBUG, id_str(node) << " dies with " 
       << inqs[node]->size() << " msgs in queue\n");
   inqs.erase(node);
}

node_id_t Net::get_rand_server() {
   return dssim->get_rand_server();
}
std::set<node_id_t> Net::get_serv_ids(node_id_t nid) {
   return dssim->get_serv_ids(nid);
}
bool Net::timop(tick_t base, tick_t timo) {
   // Even though uniform_int_distribution takes an int, it is unsigned
   tick_t rtimo = timo 
      + dssim->rand_int(0, (int)nearbyint(0.3*timo)) 
      - (int)nearbyint(0.15*timo);
   if(rtimo <= 1) rtimo = timo;
   return now() >= (base + rtimo);
}
bool Net::delay() {
   return dssim->delay();
}

const char* Net::id_str(node_id_t n) {
   return dssim->id_str(n);
}
tick_t Net::now() {
   return dssim->now();
}

std::unique_ptr<net_msg_t> Net::recv(node_t* me) {
   if( inqs.count(me->get_nid()) == 0 ) {
      l::og(l::WARN, "recv: No inq for node %s\n", me->id_str());
      return nullptr;
   }
   auto inq = inqs[me->get_nid()].get();
   if( inq->size() > 0 ) {
      if(dssim->shuffle()) {
         std::shuffle( std::begin(*inq), std::end(*inq), dssim->rand_gen );
      }
      stat.tot_msg_recv++;
      stat.recvs[me->get_nid()]++;
      std::unique_ptr<net_msg_t> msg = std::move(inq->front());
      inq->pop_front();
      l::og(l::DEBUG,
            "%s %s from %s R(%03lld)\n",
            me->id_str(), msg->descr, id_str(msg->src), msg->pkt_num);
      return std::move(msg);
   }
   return nullptr;
}

bool Net::send(node_t *src, node_id_t dst, 
               std::unique_ptr<net_msg_t> msg) {
   auto inqit = inqs.find(dst);
   MASSERT(inqs.count(src->get_nid()) > 0, "src unregisterd to network");
   if( inqit == inqs.end() ) {
      // Can happen when you send to dead node
      l::og(l::DEBUG, "send: No inq for node %s\n", id_str(dst));
      msg = nullptr;
      return false;
   }
   // "NIC" fills in pkt_num and source of message
   // NB: Clients can copy messages before sending them, so best to
   //  assign number here rather than at message creation
   msg->pkt_num = net_msg_t::_pkt_num++;
   msg->sent_tick = now();
   msg->src = src->get_nid();
   stat.tot_msg_send++;
   stat.sends[src->get_nid()]++;
   // Just log recv
   //l::og(l::DEBUG, 
   //       "%s %s to %s S(%03lld)\n", 
   //       src->id_str(), msg->descr, id_str(dst), msg->pkt_num);
   inqit->second->push_back(std::move(msg));
   return true;
}

void Net::drop(node_t* me, const net_msg_t& msg, const char* reason){
   l::og(l::DEBUG, "%s %s Drop(%03lld)\n     ", 
         me->id_str(), reason, msg.pkt_num);
   msg.pr(l::og(l::DEBUG));
   l::og(l::DEBUG) << std::endl;
}

bool Net::any_pending() const {
   for(auto& inq : inqs) {
      if(inq.second->size() > 0) {
         return true;
      }
   }
   return false;
//   l::og(l::DEBUG) << me->id_str() << " inq sz " << inq->size() << std::endl;
}

void Net::rpc_incr(int rpc_num) {
   stat.rpc_cnt[rpc_num]++;
   stat.rpc_tot++;
}

// Hmm, don't want to include paxos.h
extern std::unordered_map<int, std::string> paxrpc2str;
std::ostream& Net::pr_stat(std::ostream& os) const {
   os << "NET";
   os << " Send: "<< stat.tot_msg_send 
      << " Recv: " << stat.tot_msg_recv
      << " RPC: " << stat.rpc_tot;
   return os;
}

std::ostream& Net::pr_rpc_stat(std::ostream& os) const {
   // Print RPC breakdown, sorted by most popular
   std::vector<int> rpc_ids;
   rpc_ids.reserve(stat.rpc_cnt.size());
   for(const auto& rcnt : stat.rpc_cnt) {
      rpc_ids.push_back(rcnt.first);
   } 
   std::sort(std::begin(rpc_ids), std::end(rpc_ids), 
             [=](int a, int b) {
                // Iterators via find to keep const
                return stat.rpc_cnt.find(a)->second > stat.rpc_cnt.find(b)->second;
             });

   for(auto rpc_id : rpc_ids) {
      uint64_t rpc_cnt = stat.rpc_cnt.find(rpc_id)->second;
      os << "\n" << std::setw(2) << rpc_id
         << ": " << paxrpc2str[rpc_id]
         << ": " << std::setw(5) << rpc_cnt
         << " (" << std::setw(5) << std::setprecision(4) 
         << 100.0*rpc_cnt/stat.rpc_tot
         << "%)";
   }
   return os;
}

std::ostream& operator<<(std::ostream& os, const Net& net){
   net.pr_stat(os);
   return os;
}

uint64_t Net::sends(node_id_t nid) {
   return stat.sends[nid];
}

uint64_t Net::recvs(node_id_t nid) {
   return stat.recvs[nid];
}

int Net::get_initial_num_servers() {
   return dssim->get_initial_num_servers();
}

bool Net::use_fake_init_vc() {
   return dssim->use_fake_init_vc();
}
