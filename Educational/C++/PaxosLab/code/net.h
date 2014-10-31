// net.h: simulated network functionality
#pragma once
#include <unordered_map>
#include <set>
#include <deque>
#include <random>
#include <memory>
#include <iostream>
#include <iomanip>

#include "node.h"
// Base type for a network message
// Fill it with goodies to make debugging/logging easier
struct net_msg_t {
   // Filled in by "NIC" (net::send)
   node_id_t src; 
   // No need for dst
   // System-wide unique packet number, for debugging, written in Net::send
   uint64_t pkt_num;
   // Tick packet was sent, written in Net::send.  Note that we assume
   // (at least roughly) synchronized clocks.  We interpret one node's 
   // clock on another node.
   tick_t sent_tick;
   // Text description of the message 10 chars exactly
   // C-style string because these are structs and descr is static
   const char* descr;
   net_msg_t(const char* _descr) { 
      MASSERT(_descr!=NULL, "Can't construct message with NULL descr");
      descr = _descr; 
   }
   ~net_msg_t() {}
   virtual void pr(std::ostream& os) const {
      os << "{" 
         << "p(" << std::setfill('0') << std::setw(4) << pkt_num << ") " 
         << descr << "}";
   }
   // NOT thread safe
   static uint64_t _pkt_num;
};
std::ostream& operator<<(std::ostream& os, const net_msg_t& msg);


// Forward declaration
class dssim_t;

class Net {
public:
   Net(dssim_t*);
   ~Net();

   node_id_t get_new_nid();
   // Say you are going to connect to network, so we can detect bad IDs
   void announce(node_id_t);
   void die(node_id_t);

   // These three functions call through to dssim
   // Get me the address of any server, e.g., from broadcast RPC
   //  with DNS load distribution
   node_id_t get_rand_server();
   // We are assuming that any node can get the ids of all running servers.
   // Broadcast RPC or a non-replicated service would provide it.
   // For convenience, remove the passed in node_id, because we mostly want
   // other servers
   std::set<node_id_t> get_serv_ids(node_id_t);

   // What time is it?
   tick_t now();
   // Just a logging function
   const char* id_str(node_id_t);
   // Returns true if we have timed out.  We have a function to 
   // do a bit of randomization (15%) to avoid deterministic 
   // packet storms
   bool timop(tick_t base, tick_t timo);
   // Randomly decide to recv nothing 
   bool delay();

   // Sending and receiving network messages
   std::unique_ptr<net_msg_t> recv(node_t* _me);
   bool send(node_t* src, node_id_t _dst, std::unique_ptr<net_msg_t>);
   // A pseudo-operation to aid logging
   void drop(node_t* me, const net_msg_t& msg, const char* reason);
   // Keep simulation alive while pending messages
   bool any_pending() const;

   // stats
   void rpc_incr(int rpc_num);
   // Printing statistics for network
   std::ostream& pr_stat(std::ostream& os) const;
   std::ostream& pr_rpc_stat(std::ostream& os) const;
   // Query stats
   uint64_t sends(node_id_t);
   uint64_t recvs(node_id_t);
   int get_initial_num_servers();
   bool use_fake_init_vc();
private:
   // No Copy
   Net(const Net&) = delete;
   Net& operator=(const Net&) = delete;

   dssim_t* dssim;
   // State for incoming messages
   std::unordered_map<node_id_t, 
      std::unique_ptr<std::deque<std::unique_ptr<net_msg_t>>>>inqs;

   struct {
      uint64_t tot_msg_send;
      uint64_t tot_msg_recv;
      std::unordered_map<node_id_t, uint64_t> sends;
      std::unordered_map<node_id_t, uint64_t> recvs;
      std::unordered_map<int, uint64_t> rpc_cnt;
      uint64_t rpc_tot;
   } stat;
};
std::ostream& operator<<(std::ostream& os, const Net& net);
