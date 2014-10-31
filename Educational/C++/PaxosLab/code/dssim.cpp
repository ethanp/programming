#include <map>
#include <vector>
#include <unordered_set>
#include <string>
#include <exception>
#include <algorithm>
#include <fstream>
#include <sstream>
#include <cstdio>
#include <string>
#include <regex>

#include "dssim.h"
#include "make_unique.h"
#include "node.h"
#include "net.h"
#include "log.h"
#include "net.h"
#include "word_vec_pax.h"
#include "massert.h"
#include "paxserver.h"

Sched::Sched(){
}
void Sched::add(tick_t tick, nidid_t nidid, Sched::aid_t aid) {
//   events.emplace(Sched::event(tick, nidid, aid));
}
bool Sched::eventp(tick_t tick) {
   if(events.size() == 0) return false;
   return events.back().tick <= tick;
}
Sched::event Sched::pop() {
   Sched::event ev = events.back();
   events.pop_back();
   return ev;
}

void Sched::init(const std::string& str) {
   std::size_t open, close;
   open = str.find("{");
   close = str.find("}");
   while( open != std::string::npos
          && close != std::string::npos ) {
      // Cut open and close braces
      std::string segment = str.substr(open + 1, close-open-1);
      int ti;
      char ni[64];
      char ai[64];
//      fprintf(stderr, "sscanf--%s\n", segment.c_str());
      std::sscanf(segment.c_str(), "%d, %64[^','], %64s", &ti, ni, ai);
//      std::fprintf(stderr, "%d--%s--%s\n", ti, ni, ai);
      events.emplace_back((tick_t)ti, ni, ai);
      char* end;
      nidid2nid[ni] = std::strtol(ni, &end, 10);

      open = str.find("{", close);
      close = str.find("}", open);
   }
   // Put the lowest tick items on the end for easy pop_back
   std::sort(std::begin(events), std::end(events), 
             [](const event& a, const event& b) {
                return a.tick >= b.tick;
             });
}

class dssim_ex_t: public std::exception {
   virtual const char* what() const throw() {
      return "dssim exception";
   }
};

dssim_t::dssim_t() {
   // Time doesn't start at 0!  0 is an illegal time
   ticks = 1ULL;
}
dssim_t::~dssim_t() {
   nodes.clear();
}

dssim_t::Config::Config() {
}

void dssim_t::make_server(node_id_t nid) {
   pax_serv_timo ps_timo;
   ps_timo.vca_timo = config.serv_vca_timo;
   ps_timo.dead_timo = config.serv_dead_timo;
   ps_timo.heartbeat_timo = config.serv_heartbeat_timo;
   auto server = std::make_unique<paxserver>(net, nid, ps_timo,
                                             std::make_unique<po_word_vec>());
   // l::og(l::DEBUG, "Create server %s\n", node->id_str());
   net->announce(nid);
   if(nodes[server->get_nid()] != nullptr) {
      LOG(l::WARN, "Non-null entry nid:" << server->get_nid() << "\n");
   }
   nodes[server->get_nid()] = std::move(server);
}

void dssim_t::make_client(node_id_t nid, std::string prefix) {
   auto node = std::make_unique<pc_word_vec>(
      net, nid, config.client_timo, config.client_switch_ntimo,
      prefix, (uint64_t)config.nclient_req);
   //l::og(l::DEBUG) << "Create client " << node->id_str()
   //               << " " << firstlet << "\n";
   net->announce(nid);
   nodes[node->get_nid()] = std::move(node);
}

void dssim_t::configure(Net* _net, const dssim_t::Config& _config) {
   net = _net;
   config = _config;
   rand_gen.seed(config.rand_seed);
   rand_percent = std::make_unique<std::uniform_int_distribution<int>>(0, 100);

   // Initially, servers do know about each other.  This corresponds to
   // a service starting by booting some machines who then contact
   // each other for a view change
   MASSERT(config.nservers > 0, "impossible");
   std::unordered_set<node_id_t> serv_ids;
   for(int i = 0; i < config.nservers; ++i) {
      serv_ids.insert(net->get_new_nid());
   }
   for(auto& nid : serv_ids) {
      make_server(nid);
   }

   // Gosh, isn't there a simpler way?
   std::vector<std::string> prefix;
   if(config.nclients <= 26) {
      prefix.resize(26);
      int i = 0;
      for(char c = 'a'; c <= 'z'; ++c) {
         prefix[i++] = std::string(1, c);
      }
   } else if(config.nclients <= 26*26) {
      prefix.resize(26*26);
      int i = 0;
      for(char b = 'a'; b <= 'z'; ++b) {
         for(char c = 'a'; c <= 'z'; ++c) {
            prefix[i++] = std::string(1, b) + c;
         }
      }
   } else if(config.nclients <= 26*26*26) {
      prefix.resize(26*26*26);
      int i = 0;
      for(char a = 'a'; a <= 'z'; ++a) {
         for(char b = 'a'; b <= 'z'; ++b) {
            for(char c = 'a'; c <= 'z'; ++c) {
               prefix[i++] = std::string(1, a) + b + c;
            }
         }
      }
   } else {
      std::fprintf(stderr, "Too many clients %d (max is %d)\n", 
                   config.nclients, 26*26*26);
      exit(79);
   }
   for(int i = 0; i < config.nclients; ++i) {
      node_id_t nid = net->get_new_nid();
      make_client(nid, prefix[i]);
   }
}

template<typename Iter, typename RandomGenerator>
Iter select_randomly(Iter start, Iter end, RandomGenerator& g) {
    std::uniform_int_distribution<int> dis(0, std::distance(start, end) - 1);
    std::advance(start, dis(g));
    return start;
}
node_id_t dssim_t::get_rand_server() {
   // Conceptually a broadcast message, or a name service
   std::vector<node_id_t> servers;
   for(auto& node_pair : nodes) {
      if(node_pair.second->server()) 
         servers.push_back(node_pair.second->get_nid());
   }
   auto it = select_randomly(std::begin(servers), std::end(servers), rand_gen);
   if(it == servers.end()) return (node_id_t)0;
   return *it;
}
std::set<node_id_t> dssim_t::get_serv_ids(node_id_t nid) {
  std::set<node_id_t> servers;
   for(auto& node_pair : nodes) {
      if(node_pair.second->server()) 
         servers.insert(node_pair.second->get_nid());
   }
   servers.erase(nid);
   return servers;
}

char buf[16];
const char* dssim_t::id_str(node_id_t n) {
   if(nodes.count(n) == 0) {
      snprintf(buf, 16, "%02d unk", n);
      return buf;
   }
   return nodes[n]->id_str();
}

// Should I delay receipt of this message?
bool dssim_t::delay() {
   return (*rand_percent)(rand_gen) < config.prob_delay;
}
// Should I shuffle queue?
bool dssim_t::shuffle() {
   return (*rand_percent)(rand_gen) < config.prob_shuffle;
}
// Inclusive
int dssim_t::rand_int(int lo, int hi) {
   MASSERT(lo >= 0 && hi >= 0, 
           "Sadly, std::uniform_int_distribution only works on positive integers");
   std::uniform_int_distribution<int> uid(lo, hi);
   return uid(rand_gen);
}
std::unordered_map<int, std::string> aid2str ={
   {Sched::A_DIE,     "die"},
   {Sched::A_SJOIN,   "sjoin"},
   {Sched::A_CJOIN,   "cjoin"},
   {Sched::A_PAUSE,   "pause"},
   {Sched::A_UNPAUSE, "unpause"},
};
// XXX Check IDs to make sure they are legal
void dssim_t::do_events() {
   while(config.sched.eventp(ticks)) {
      Sched::event ev = config.sched.pop();
      node_id_t nid = config.sched.nidid2nid[ev.nidid];
      LOG(l::DBG_EV, "Event tick:" << ticks
          << " nidid:" << ev.nidid
          << " nid:" << nid
          << " aid:" << aid2str[ev.aid]
          << "\n");
      switch(ev.aid){
         case Sched::A_DIE:
            net->die(nid);
            nodes.erase(nid);
            break;
         case Sched::A_SJOIN: {
            node_id_t nid = net->get_new_nid();
            make_server(nid);
         }
            break;
         case Sched::A_CJOIN: {
            node_id_t nid = net->get_new_nid();
            make_client(nid, "dynamic");
         }
            break;
         case Sched::A_PAUSE:
            paused.insert(nid);
            break;
         case Sched::A_UNPAUSE:
            if(paused.count(nid) > 0) {
               paused.erase(nid);
               nodes[nid]->set_unpaused_tick(now());
            }
            break;
      }
   }
}
bool dssim_t::tick() {
   do_events();
   bool going = false;
   for(auto& node : nodes) {
      if(paused.count(node.second->nid) == 0) {
         bool result = node.second->tick();
         going = going || result;
      } else {
         going = true;
      }
   }
   ticks++;
   if((ticks % 99) == 0) {
      LOG(l::DBG_EV, "Tick:" << ticks << "\n");
   }
   if(going == false) {
      // If we have a disconnected server, wait for him to time out and view change his way in
      node_id_t pr = (node_id_t)0;
      for(auto& node : nodes) {
         if(node.second->primary()) {
            pr = node.first;
         }
      }
      if(pr != (node_id_t)0) {
         const std::unique_ptr<node_t>& pr_node = nodes[pr];
         for(auto& node : nodes) {
            if(node.second->server()
               && !pr_node->in_view(node.first)) {
               LOG(l::DBG_EV, "Continue " << node.first << " not in view\n");
               going = true;
               break;
            }
         }
      }
   }
   return going;
}

tick_t dssim_t::now() {
   return ticks;
}

int dssim_t::get_initial_num_servers() {
   return config.nservers;
}

bool dssim_t::use_fake_init_vc() {
   return config.fake_init_vc;
}

// Want to print any differences (even if there is no primary, which
// there might not be if we are in the middle of a view change
std::ostream& dssim_t::pr_agreement(std::ostream& os, bool verbose) {
   os << "Total Ticks: " << ticks;
   paxobj* first_po = nullptr;
   const char* first_id;
   bool all_match = true;
   for(auto& node : nodes) {
      if(node.second->server()) {
         auto bk_obj = node.second->get_paxobj();
         if(first_po == nullptr) {
            first_po = bk_obj;
            first_id = node.second->id_str();
         } else {
            if(!first_po->eq(bk_obj)) {
               os << " DIFFERENT\n\t";
               if(all_match == true) {
                  os << first_id << " and " << node.second->id_str() << "\n";
                  all_match = false;
               }
            }
         }
      }
   }
   if(all_match) {
      os << "\nAll BK Match, groovy\n";
   } else if(verbose) {
      for(auto& node : nodes) {
         if(node.second->primary()) {
            os << "Primary: " << node.second->id_str() << "\n";
         }
      }
      for(auto& node : nodes) {
         if(node.second->server()) {
            auto bk_obj = node.second->get_paxobj();
            os << node.second->id_str() << " " << *bk_obj << "\n";
         }
      }
   }
   return os;
}

#if 0
std::ostream& dssim_t::pr_agreement(std::ostream& os, bool verbose) {
   os << "Total Ticks: " << ticks;
   // Find primary's paxobj and compare if equal with backups
   paxobj* pr_obj = nullptr;
   const char* pr_id = nullptr;
   for(auto& node : nodes) {
      if(node.second->primary()) {
         pr_obj = node.second->get_paxobj();
         pr_id = node.second->id_str();
      }
   }
   if(!pr_obj) {
      os << "\n\tNo primary";
      // Might want to print paxobj from all servers
   } else {
      bool all_match = true;
      for(auto& node : nodes) {
         if(node.second->server() && !node.second->primary()) {
            auto bk_obj = node.second->get_paxobj();
            if(!pr_obj->eq(bk_obj)) {
               if(all_match == true) {
                  os << "\nPR " << pr_id << "\n\t";
                  if(verbose) {
                     os << *pr_obj;
                  }
               }
               all_match = false;
               os << "\nBK ";
               os << node.second->id_str();
               os << " DIFFERENT\n\t";
               os << *bk_obj;
            }
         }
      }
      if(all_match) {
         os << "\nAll BK Match, groovy";
         if(verbose) {
            os << "\nPR " << pr_id << "\n\t";
            os << *pr_obj;
         }
      }
   }
   os << "\n";
   return os;
}
#endif

std::ostream& dssim_t::pr_short_stat(std::ostream& os) {
   pr_agreement(os, true);
   return os;
}

std::ostream& dssim_t::pr_short_short_stat(std::ostream& os) {
   pr_agreement(os, false);
   return os;
}

std::ostream& dssim_t::pr_stat(std::ostream& os) {
   pr_agreement(os, true);
   // Print nodes in order of node_id_t
   std::vector<node_id_t> keys;
   keys.reserve(nodes.size());
   for(auto& kv : nodes) {
      keys.push_back(kv.first);
   } 
   std::sort(std::begin(keys), std::end(keys));
   // Print servers
   os << "SERVERS";
   for(auto& key : keys) {
      if(nodes[key]->server()) {
         os << *nodes[key];
      }
   }
   os << "\nCLIENTS";
   for(auto& key : keys) {
      if(!nodes[key]->server()) {
         os << *nodes[key];
      }
   }
   os << "\n";
   return os;
}

std::ostream& operator<<(std::ostream& os, const dssim_t::Config& con){
   os << "Paxos Simulation Configuration\n";
   os << "\tnclients:" << con.nclients
      << " nrequests:" << con.nclient_req
      << " client timo:" << con.client_timo
      << " client ntimo:" << con.client_switch_ntimo
      << "\n\tnservers:" << con.nservers
      << " serv_vca_timo:" << con.serv_vca_timo
      << " serv_dead_timo:" << con.serv_dead_timo
      << " serv_heartbeat_timo:" << con.serv_heartbeat_timo
      << "\n";
   os << "\trand_seed:" << con.rand_seed
      << " prob_delay:" << con.prob_delay
      << " prob_shuffle:" << con.prob_shuffle
      << " log_level:" << con.log_level
      << std::endl;
   return os;
}
