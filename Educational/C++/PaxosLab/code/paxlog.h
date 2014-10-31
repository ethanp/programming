// paxlog.h - Defines for the paxos log
#pragma once

#include "node.h"
#include "paxtypes.h"
#include "paxobj.h"

// Servers keep log of operations, sequentially ordered in each view
// by viewstamp
class Paxlog {
  public:
   struct tup {
      node_id_t src;
      rid_t rid;
      viewstamp_t vs;
      paxobj::request request;
      // Remember how many servers for this view.
      unsigned int serv_cnt;
      // Once resp_cnt > maj servers, primary can execute request when
      // it is next in timestamp order
      unsigned int resp_cnt;
      // if resp_cnt == serv_cnt && executed, can erase from log
      bool executed;
      // Tick tup was added (via log).  If we fail to execute our
      // oldest record for too long, we try to catchup with primary
      // XXX Not currently used & not totally sure why.  A different time out?
      tick_t added_tick;
      tup(node_id_t _src, rid_t _rid, const viewstamp_t& _vs, 
             paxobj::request _request, unsigned int _serv_cnt, tick_t now);
      tup(tup& t); 
   };
   Paxlog();
   Paxlog(const Paxlog&);
   Paxlog& operator=(const Paxlog&);
//   Paxlog(Paxlog&&);
   ~Paxlog() {l.resize(0);};

   // NB: Primary recognizes duplicate requests using rid
   //   but backups recognize them via duplicate viewstamps
   bool find_rid(node_id_t src, rid_t rid);
   void log(node_id_t src, rid_t rid, const viewstamp_t& vs, 
               paxobj::request req,  unsigned int serv_cnt, tick_t now);
   // Find entry with viewstamp vs and increment is response count
   bool incr_resp(const viewstamp_t& vs);
   std::vector<std::unique_ptr<tup>>::iterator begin() {
      return l.begin();
   }
   std::vector<std::unique_ptr<tup>>::iterator end() {
      return l.end();
   }
   bool next_to_exec(std::vector<std::unique_ptr<tup>>::iterator);
   // latest_exec is important because it represents the highest vs
   // we have executed and hence the next viewstamp we can execute.
   // On a vc update this to ts==0 in the new view to prevent
   // unsuccessful operations in old view from getting committed 
   viewstamp_t latest_exec() const { return last_exec_vs; }
   void set_latest_exec(const viewstamp_t& vs) { last_exec_vs = vs; }
   // Track the latest accept vs independently because we might get accept
   // messages while we have a hole in our log and when the hole gets
   // filled in (by a getstate), we want to remember what to accept
   // last_accept_vs should increase monotonically.  Once the primary accepts,
   // the accept persists forever
   viewstamp_t latest_accept() const { return last_accept_vs; }
   void set_latest_accept(const viewstamp_t& vs) { 
       if(vs > last_accept_vs) last_accept_vs = vs; 
   }
   bool empty() const;
   std::size_t size() const {return l.size();}
   // Get entry at vs.  MUST call contains first and be sure entry @ vs exists
   const tup* get_tup(const viewstamp_t& vs);
   // Can we execute entry? pass in its result
   bool execute(std::unique_ptr<tup>&);
   // Love the const reference to a unique_ptr
   void trim_front(const std::function<bool (const std::unique_ptr<tup>&)>);

  private:
   // Sequential log, sorted by viewstamp
   std::vector<std::unique_ptr<tup>> l;
   // What is the last viewstamp I executed.  I can only execute its
   // sucessor which is either the next ts in this view, or ts 0 in
   // "the next" view (which could be any viewid higher than our current)
   viewstamp_t last_exec_vs;
   viewstamp_t last_accept_vs;

   friend std::ostream& operator<<(std::ostream& os, const Paxlog& plog);
};
std::ostream& operator<<(std::ostream& os, const Paxlog::tup& tup);
std::ostream& operator<<(std::ostream& os, const Paxlog& plog);
