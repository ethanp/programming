#pragma once

#include "paxmsg.h"
#include "log.h"

class paxclient : public node_t {
public:
	paxclient(Net* _net, node_id_t _nid, int _timeout, int _switch_ntimo);
	virtual ~paxclient();

   // A request id, request object and its callback
   typedef std::unique_ptr<std::function<void(std::string)>> cb;
   struct req_rec {
   req_rec(paxclient* _client, rid_t _rid, const viewid_t& _vid,
           paxobj::request _request, cb _reply_cb);
      bool check_timo();
      void reset_timo();
      std::ostream& pr(std::ostream& os) const;

      paxclient* client;
      int timo_cnt;     // How many timeouts?
      tick_t tick_last_sent;
      rid_t rid;
      viewid_t vid;
      paxobj::request request;
      cb reply_cb;

      // Less than operator for ordering
      bool operator<(const struct req_rec& o) {
         return rid < o.rid;
      }
   };

   // Function object for ordering req_rec
   struct req_rec_less {
      bool operator()(const std::shared_ptr<struct req_rec> &a,
              const std::shared_ptr<struct req_rec> &b) const {
        return *a < *b;
     }
   };


   friend std::ostream& operator<<(std::ostream& os, const req_rec&);
   struct req_cb {
      req_cb( std::unique_ptr<paxobj::op> _req,  cb _reply_cb ) {
         request = std::move(_req);
         reply_cb = std::move(_reply_cb);
      }
      std::unique_ptr<paxobj::op> request;
      cb reply_cb;
   };

   ////////////////////////////////////////////////
   // Clients must override the following functions
   // Invoke a request for the paxos object and provide a callback 
   // for the response.  
   virtual std::unique_ptr<req_cb> work_get() = 0;
   // Return true when finished with work
   virtual bool work_done() = 0;

   // Return false when done
	virtual bool tick(void) override;
   virtual const char* id_str() const {
      return my_id;
   }

protected:
   std::ostream& pr_stat(std::ostream&) const override;

private:
   // Main functions
   void do_timo();
   void dispatch(paxmsg_t* paxmsg);
   // Message handlers
   void execute_success(struct execute_success* ex_succ);
   void execute_fail(struct execute_fail* ex_fail);
   // Helpers
   std::shared_ptr<paxclient::req_rec> next_req_rec();
   std::shared_ptr<req_rec> find_kill_req_rec(rid_t rid);
   void erase(rid_t rid);


   // Outstanding execute_requests (not getview's)
   // Our client only has one outstanding request at a time.
   // THOUGH with timeouts, that isn't strictly true
   // We could do more than one, and probably should at some point
   // We'd have to put getview's in this set and poll them all for timeout
   std::set<std::shared_ptr<req_rec>, struct req_rec_less> out_req;
   // timeout in ticks, and after switch_ntimo timeouts, switch servers
   int timo;
   int switch_ntimo;
   // View information
	viewid_t local_vid;
   node_id_t primary;
   // Request counter
	rid_t local_rid;

   // Identifier of the form C02\0
   char my_id[5];

	struct {
		uint64_t started_op;
		uint64_t success_op;
	} stat;
};
