// dssim.h: Simulate a distributed system
#pragma once
#include <unordered_map>
#include <set>
#include <unordered_set>
#include <deque>
#include <random>
#include <memory>
#include <iostream>
#include <cstring>

#include <stdint.h>
#include "node.h"
#include "log.h"

// Forward declaration as these classes are mutually dependent
//  (not logically, but practically)
class Net;

class Sched {
  public:
   enum aid_t {
      A_DIE,
      A_SJOIN,
      A_CJOIN,
      A_PAUSE,
      A_UNPAUSE
   };
   // Nodes are identified by number or symbolically by 
   // letter.  So a layer of indirection to node_id_t
   typedef std::string nidid_t;
   struct event {
      event(tick_t t, const char* nidid_str, const char* aid_str) {
         tick = t;
         nidid = nidid_str;
         if(!strcasecmp(aid_str, "die")) {
            aid = A_DIE;
         } else if(!strcasecmp(aid_str, "sjoin")) {
            aid = A_SJOIN;
         } else if(!strcasecmp(aid_str, "cjoin")) {
            aid = A_CJOIN;
         } else if(!strcasecmp(aid_str, "pause")) {
            aid = A_PAUSE;
         } else if(!strcasecmp(aid_str, "unpause")) {
            aid = A_UNPAUSE;
         } else {
            std::cerr << "Syntax error\n";
         }
      }
      tick_t tick;
      nidid_t nidid;
      aid_t aid;
   };
   Sched();
   void init(const std::string& str);
   bool eventp(tick_t);
   event pop();
   std::unordered_map<nidid_t, node_id_t> nidid2nid;
  private:
   void add(tick_t, nidid_t, aid_t);
   // Smallest tick value at end
   std::vector<event> events;
};


class dssim_t {
public:
   struct Config {
      Config();
      int nclients;
      int nclient_req;
      int client_timo;
      int client_switch_ntimo;
      int nservers;
      bool fake_init_vc;
      // Extra time to wait for responses to view_change_arg
      int serv_vca_timo;
      // No response in this amount of time, consider server dead, view change
      int serv_dead_timo;
      // How often to send heartbeat messages
      int serv_heartbeat_timo;
      // Should be unsigned, but arg parser doesn't really support it
      int rand_seed;
      // Probability that a packet's recv will be delayed
      int prob_delay;
      // Probability that a node's input msg queue will be shuffled
      int prob_shuffle;
      // {{T,N,A}...} tick, node, action triples
      std::string str_sched;
      Sched sched;
      std::string outfile;
      std::string log_level;
   };
   dssim_t();
   ~dssim_t();

   // words_fname: File name of words
   // nwords 0 is all words, non-zero limits client work
   // Should decouple words from paxclient
   void configure(Net* net, const Config& _config);

   // These functions called via Net
   // Get me the address of a random server, e.g., from broadcast RPC
   // It is important that repeated calls not always return the same
   // server id
   node_id_t get_rand_server();
   // This call is an optimization of get_rand_server that returns all 
   // current servers.  This could be implemented by broadcast RPC or 
   // a service that monitors whether nodes are up.
   std::set<node_id_t> get_serv_ids(node_id_t);

   // Get id string for node
   const char* id_str(node_id_t n);
   // Should I delay receipt of this message?
   bool delay();
   // Should I shuffle queue?
   bool shuffle();
   // Source of randomness for nodes/network INCLUSIVE
   int rand_int(int lo, int hi);

   // Do something.  Return false if we are done
   bool tick();
   // What time is it now?
   tick_t now();

   // Print short stats
   std::ostream& pr_short_stat(std::ostream& os);
   std::ostream& pr_short_short_stat(std::ostream& os);
   // Print more statistics 
   std::ostream& pr_stat(std::ostream& os);

   // Random generator, here for use in Net::recv to shuffle
   std::default_random_engine rand_gen;

   int get_initial_num_servers();
   bool use_fake_init_vc();
private:
   // No Copy
   dssim_t(const dssim_t& rhs) = delete;
   dssim_t& operator=(const dssim_t& rhs) = delete;
   std::ostream& pr_agreement(std::ostream& os, bool verbose);

   void make_server(node_id_t nid);
   void make_client(node_id_t nid, std::string prefix);
   void do_events();

   // Master store for nodes
   std::unordered_map<node_id_t, std::unique_ptr<node_t>> nodes;
   // Paused nodes
   std::unordered_set<node_id_t> paused;

   // For calculating events specified by percentages
   std::unique_ptr<std::uniform_int_distribution<int>> rand_percent;
   Config config;
   Net* net;

   tick_t ticks;
};

std::ostream& operator<<(std::ostream& os, const dssim_t::Config&);
