#include <memory>

#include "paxlog.h"
#include "make_unique.h"
#include "massert.h"
#include "log.h"

//////////////////////////////////////////////////////
// Paxlog
Paxlog::Paxlog() {
   last_exec_vs = {};
   last_accept_vs = {};
}
Paxlog::Paxlog(const Paxlog& pl) {
   if (this == &pl) return;
   last_exec_vs = pl.last_exec_vs;
   last_accept_vs = pl.last_accept_vs;
   l.resize(0);
   l.resize(pl.l.size());
   unsigned i = 0;
   for(auto& ple : l) {
      ple = std::make_unique<Paxlog::tup>(*pl.l[i]);
      i++;
   }
}
Paxlog& Paxlog::operator=(const Paxlog& pl) {
   if (this == &pl) return *this;
   last_exec_vs = pl.last_exec_vs;
   last_accept_vs = pl.last_accept_vs;
   l.resize(0);
   l.resize(pl.l.size());
   unsigned i = 0;
   for(auto& ple : l) {
      ple = std::make_unique<Paxlog::tup>(*pl.l[i]);
      i++;
   }
   return *this;
}

bool Paxlog::next_to_exec(std::vector<std::unique_ptr<tup>>::iterator it) {
   return ((*it)->executed == false)
      && (*it)->vs.sucessor(last_exec_vs);
}

bool Paxlog::empty() const {
   return l.size() == 0UL;
}

bool Paxlog::execute(std::unique_ptr<tup>& ex) {
   ex->executed = true;
   last_exec_vs = ex->vs;
   return true;
}

bool
Paxlog::incr_resp(const viewstamp_t& vs) {
   for(auto entry = l.begin(); entry != l.end(); ++entry) {
      if((*entry)->vs == vs) {
         (*entry)->resp_cnt++;
         return true;
      }
   }
   return false;
}

// Apply trim function from the beginning and the set of contiguous
//  elements for which it is true are trimmed from the front
void Paxlog::trim_front(std::function<bool (const std::unique_ptr<tup>&)> trimf) {
   auto entry = l.begin();
   for(; entry != l.end(); ++entry) {
      if(!trimf(*entry)) break;
   }
   for(auto it = l.begin(); it != entry; ++it) {
      LOG(l::DEBUG, "Trim " << **it << "\n");
   }
   if(entry != l.begin()) {
      l.erase(l.begin(), entry);
   }
}


bool Paxlog::find_rid(node_id_t src, rid_t rid) {
   // See if this request is a duplicate
   for(auto entry = l.begin(); entry != l.end(); ++entry) {
      if((*entry)->rid == rid && (*entry)->src == src) 
         return true;
   }
   return false;
}

const Paxlog::tup* Paxlog::get_tup(const viewstamp_t& vs) {
   for(const auto& entry : l) {
      if(entry->vs == vs) return entry.get();
   }
   return nullptr;
}

void Paxlog::log(node_id_t src, rid_t rid, const viewstamp_t& vs, 
                    paxobj::request req, unsigned int serv_cnt, tick_t now) {
   // Insert records in viewstamp order
   //  Operations can only be executed in viewstamp sucessor order,
   //  but they can always be enqueued
   MASSERT(serv_cnt, "Can't have view with zero servers");
   MASSERT(req != nullptr, "Must have non-null request");
   auto logit = l.begin();
   while(logit != l.end() && (*logit)->vs < vs)
      ++logit;
   l.emplace(logit, std::make_unique<Paxlog::tup>(src, rid, vs, std::move(req),
                                                     serv_cnt, now));
   LOG(l::DEBUG, "Log: " << *l[l.size()-1] << "\n");
   
#if 0
   // debuging code for sake of understanding
   if(l.size() >= 2) {
      for(logit = l.begin(); (logit+1) != l.end(); ++logit) {
         if(!(*(logit+1))->vs.sucessor((*logit)->vs)) {
            LOG(l::WARN, "Paxlog hole\n" 
                << **logit << "\n" 
                << **(logit+1) << "\n");
         }
      }
   }
#endif
}

Paxlog::tup::tup(node_id_t _src, rid_t _rid, const viewstamp_t& _vs, 
                    paxobj::request _request, unsigned int _serv_cnt,
                    tick_t now) {
   src = _src;
   rid = _rid;
   vs = _vs;
   request = std::move(_request);
   serv_cnt = _serv_cnt;
   // Primary counts as 1 response
   resp_cnt = 1;
   // Not yet executed.
   executed = false;
   added_tick = now;
}
Paxlog::tup::tup(Paxlog::tup& t) {
   src = t.src;
   rid = t.rid;
   vs = t.vs;
   request = t.request;
   serv_cnt = t.serv_cnt;
   resp_cnt = t.resp_cnt;
   executed = t.executed;
   added_tick = t.added_tick;
}
