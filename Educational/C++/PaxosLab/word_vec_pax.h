// word_vec_pax: A word vector Paxos object and client
#pragma once
#include <stdint.h> 
#include <vector>
#include <string>

//#include "paxos.h"
#include "paxclient.h"

// A list words
class po_word_vec : public paxobj {
  public:
  po_word_vec() : paxobj() {}
   virtual std::unique_ptr<paxobj> clone() override;
   virtual ~po_word_vec() {}

  void push_back(const std::string& str) {
     state.push_back(str);
  }
  bool eq(const paxobj* o) const override {
     const po_word_vec* w = static_cast<const po_word_vec*>(o);
     return this->state == w->state;
  }
  void to_str(std::ostringstream& os, unsigned int start, 
              unsigned int lines, unsigned int words) const;
  std::string to_str() const override;
  private:
   std::vector<std::string> state;
};

class pc_word_vec : public paxclient {
  public:
   pc_word_vec(Net* _net, node_id_t _nid, int _timo, int _switch_ntimo,
               const std::string& _prefix, uint64_t);
   ~pc_word_vec();
   virtual std::unique_ptr<paxclient::req_cb> work_get() override;
   // Return true when finished with work
   virtual bool work_done() override;

  private:
   std::string prefix;
   std::vector<std::string> wvec;
   uint64_t max_req; // Limit total number of requests
   uint64_t curr_req; // current request number
   uint64_t cb_cnt; // Number of callbacks
};
