#include <sstream>
#include <algorithm>

#include "log.h"
#include "make_unique.h"
#include "word_vec_pax.h"

std::unique_ptr<paxobj> po_word_vec::clone() {
   return std::make_unique<po_word_vec>(*this);
}
void po_word_vec::to_str(std::ostringstream& os, unsigned int start, 
                         unsigned int lines, unsigned int words) const {
   for(unsigned int line = 0; line < lines; ++line) {
      os << "\n\t";
      for(unsigned int i = 0; i < words; ++i) {
         if(state.size() <= start+line*words+i) return;
         if(i > 0) os << ", ";
         os << state[start+line*words+i];
      }
   }
}

std::string po_word_vec::to_str() const {
   std::ostringstream oss;
   oss << "word_vec: size: ";
   oss << state.size();
   const int wperl = 10; // Words per line
   const int lines = 4;
   if(state.size() <= lines*wperl) {
      to_str(oss, 0, lines, wperl);
   } else {
      to_str(oss, 0, 2, wperl);
      if(state.size() > lines*wperl) {
         oss << "\n\t...";
      }
      if(state.size() >= lines*wperl) {
         to_str(oss, state.size()-(lines*wperl), 2, wperl);
      }
   }
   return oss.str();
}

///////////////////////////////////////////////////////
// Paxos client - word vector
pc_word_vec::pc_word_vec(Net* _net, node_id_t _nid, int _timo, 
                         int _switch_ntimo,
                         const std::string& _prefix, uint64_t _max_req) : 
   paxclient(_net, _nid, _timo, _switch_ntimo) {
   prefix = _prefix;
   max_req = _max_req;
   curr_req = 0ULL;
   cb_cnt = 0ULL;
   int i = 0;
   if(max_req <= 26) {
      wvec.resize(26);
      for(char c = 'a'; c <= 'z'; ++c) {
         wvec[i++] = prefix + '-' + c;
      }
   } else if(max_req <= 26*26) {
      wvec.resize(26*26);
      for(char b = 'a'; b <= 'z'; ++b) {
         for(char c = 'a'; c <= 'z'; ++c) {
            wvec[i++] = prefix + '-' + b + c;
         }
      }
   } else {
      // If we have more requests than this, just restart at "prefix-aaa"
      wvec.resize(26*26*26);
      for(char a = 'a'; a <= 'z'; ++a) {
         for(char b = 'a'; b <= 'z'; ++b) {
            for(char c = 'a'; c <= 'z'; ++c) {
               wvec[i++] = prefix + '-' + a + b + c;
            }
         }
      }
   }
}
pc_word_vec::~pc_word_vec() {
   wvec.clear();
}

std::unique_ptr<paxclient::req_cb>
pc_word_vec::work_get(){
   std::string work_word = wvec[curr_req++ % wvec.size()];
   auto res = std::make_unique<paxclient::req_cb>(
   // NB: Used to leak, but should be fixed, check it
      std::make_unique<paxobj::op> (
         [=](paxobj* _local_this) -> std::string {
            auto local_this = static_cast<po_word_vec*>(_local_this);
            local_this->push_back(work_word);
            return work_word;
         }, work_word),
      std::make_unique<std::function<void(std::string)>>(
         [=](std::string reply_word) {
            cb_cnt++;
            if(reply_word != work_word) {
               LOG(l::WARN,  "Yikes: " << id_str()
                   << " work_word: " << work_word
                   << " reply: " << reply_word
                   << std::endl);
            }
         }));
   LOG(l::DEBUG, id_str() << " new work:" << work_word);
   return res;
}
// Return true when finished with work
bool pc_word_vec::work_done(){
   return cb_cnt >= max_req; //curr_req >= max_req;
}
