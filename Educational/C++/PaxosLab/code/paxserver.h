//////////////////////////////////////////////////////////
// Paxos server state
#pragma once

#include "paxmsg.h"

// Nominally the view change state, but doubles as operational state for servers
struct vc_state_t {
   enum {
      ACTIVE,
      /* proposing a new view */
      MANAGER,
      /* invited to join a new view */
      UNDERLING
   } mode;
   /* last (or current) formed view */
   view_t view;
   /* last committed op at any node *//* AWFUL name, should be latest_committed*/
   viewstamp_t latest_seen;
   /* highest proposed new view-id */
   viewid_t proposed_vid;
   /* accepted new view (if any) */
   std::unique_ptr<view_t> accepted_view;
};
std::ostream& operator<<(std::ostream& os, const vc_state_t& vcs);

// State for view change manager
struct vc_mgr_t {
   // Time out values.
   tick_t start;
   tick_t last_resp;
   // It is to our advantage to create a view with as many servers
   // as we can.  So wait some.
   tick_t view_possible;
   // Manager records this at start of process, reads it when forming
   // the new view.  Everyone else's include_me is in view_change_accept
   // messages (vca, below)
   bool include_me;
   // How many servers in oldview?
   unsigned int serv_cnt;
   // Servers that have accepted vc (NOT including me)
   std::set<std::unique_ptr<struct view_change_accept>, struct vca_less> vca;
   // Servers that have accepted new_view (NOT including me)
   std::set<node_id_t> nvr;
   // Highest accept.latest 
   viewstamp_t latest;
   viewstamp_t pr_latest_exec;
   // Once conditions warrant announcing a new view, subsequent
   // view_change_accepts get that view.
   bool announce_nv;
   // Flag indicating completion of vc (if we are not primary, we don't 
   // change mode to ACTIVE
   bool complete_vc;
   // Is the old primary planning to be in the new view?
   bool old_pr_in;
   // V' is another view that might have formed concurrent with us
   // Must get a majority of them to agree that V'
   // never executed and never will execute a request
   // Might be multiple V', but we only care about latest, but that does
   // mean vprime can change during a view change
   view_t vprime;
};
std::ostream& operator<<(std::ostream& os, const vc_mgr_t& vcmgr);


///////////////////////////////////////////////////////////
// Classes to support the paxos model implementation
struct pax_serv_timo {
   // Wait for more view change accepts
   tick_t vca_timo;
   // No response in this time period, we think you are dead and view change
   tick_t dead_timo;
   // No send in this time period, send heartbeat
   tick_t heartbeat_timo;
};

class paxserver : public node_t {
public:
   paxserver(Net* _net, node_id_t _nid, const pax_serv_timo& ps_timo,
             std::unique_ptr<paxobj>);
   ~paxserver();

   // Return false when done
   virtual bool tick(void) override;
   virtual const char* id_str() const {
      return my_id;
   }
   // I want a new view, and I'll manage it
   void initiate_vc(bool include_me);

   std::ostream& pr_allstate(std::ostream&);

protected:
   std::ostream& pr_stat(std::ostream&) const override;
   std::set<node_id_t> get_other_servers(const view_t& view) {
      std::set<node_id_t> s = view.get_servers();
      s.erase(nid);
      return s;
   }

private:
   // Dispatch messages and functions for each state
   void dispatch(paxmsg_t &paxmsg);
   void execute_arg(const struct execute_arg&);
   void replicate_arg(const struct replicate_arg&);
   void replicate_res(const struct replicate_res&);
   void accept_arg(const struct accept_arg&);
   void view_change_arg(const struct view_change_arg&);
   void view_change_reject(const struct view_change_reject&);
   void view_change_accept(const struct view_change_accept&);
   void new_view_arg(const struct new_view_arg&);
   void new_view_res(const struct new_view_res&);
   void init_view_request(const struct init_view_request&);
   void init_view_arg(const struct init_view_arg&);
   // Not strictly part of paxos
   void getstate_arg(const struct getstate_arg&);
   void getstate_res(struct getstate_res&);
   // Helper functions for particular states
   unsigned int get_serv_cnt(const view_t& vid);
   std::string paxop_on_paxobj(std::unique_ptr<Paxlog::tup>& logop);
   void execute_bk_beq(const viewstamp_t& vs);
   void execute_pr_maj();
   // Viewchange helpers
   std::unique_ptr<struct view_change_accept> make_default_vca(viewid_t);
   void form_newview();
   void announce_newview();
   node_id_t choose_new_pr();
   void catchup_state(const view_t&);
   void complete_vc();
   void lets_roll_primary(const view_t&);
   void do_init_vc();
   void do_fake_init_vc();
   void become_underling(const viewid_t& newvid, node_id_t mgr, 
                         std::unique_ptr<view_t> newview);
   void new_bk(const view_t& newview);
   // View counting functions
   unsigned int cnt_vca_in_view(const view_t& view);
   bool old_maj();
   bool old_all();
   bool new_maj();
   bool vprime_maj();
   // Wrapper to send messages
   bool send_msg(node_id_t _dst, std::unique_ptr<net_msg_t>);
   void do_heartbeat();
   void do_timo();


   // Convenience functions used by dssim
   virtual bool server() const {return true;}
   virtual bool primary() const override;
   virtual bool in_view(node_id_t) const override;
   virtual paxobj* get_paxobj() override;

   // View state maintained by all servers
   vc_state_t vc_state;
   // State maintained by the view change manager
   vc_mgr_t vc_mgr;
   uint64_t ts; // timestamp
   // Server persistent log, and ordering must be dense to
   // make sure missed requests
   Paxlog paxlog;
   // The underlying object (state machine)
   std::unique_ptr<paxobj> _paxobj;
   // Timeout constants
   pax_serv_timo ps_timo;
   // Mazieres maintains the last result for each client in case the
   // primary dies before responding to client.  We don't currently
   // use this, and I have doubts as to whether it works
   struct last_tup_res {
      std::unique_ptr<Paxlog::tup> tup;
      std::string result;
   };
   std::unordered_map<node_id_t, std::unique_ptr<last_tup_res>> last_req;
   // Rely on C++11 multimap guarantee that values remain in inserted order
   std::unordered_multimap<node_id_t,rid_t> exec_rid_cache;
   // While it would probably be better to keep timestamps with
   // our rid cache and trim the cache periodically, we just keep
   // the last 500 request IDs from each client.
   // Note that even with only a single outstanding request per client
   // because of timeouts, we can have multiple requests per client.
   const unsigned int max_rid_in_cache = 500;
   // Framework for heartbeats.  Note, these are never cleared,
   // so they can have data about dead nodes, nodes not in view,
   // client nodes, etc.
   // Tick of most recent send
   std::unordered_map<node_id_t,tick_t> recent_send;
   // What is the largest timestamp from a received message from this node?
   // Note, it might not be from the most recently received message
   std::unordered_map<node_id_t,tick_t> recent_recv;

   // Identifier of the form S02\0
   char my_id[5];
   // Messages send in normal operation, execute, replicate & accept
   static std::unordered_set<int> normal_msg;
   static bool is_normal_msg(int rpc_id) {
      return normal_msg.count(rpc_id) > 0;
   }

	struct {
		uint64_t pr_started_op;
		uint64_t pr_success_op;
		uint64_t bk_started_op;
		uint64_t bk_success_op;
		uint64_t sync_writes;
      std::size_t max_paxlog;
	} stat;
};
