// Routines for Paxos view change

#include <set>
#include <algorithm>

#include "paxmsg.h"
#include "paxserver.h"
#include "make_unique.h"
#include "log.h"

///////////////////////////////////////////////////////////////////
// View change
void paxserver::initiate_vc(bool include_me) {
   MASSERT(0, "initiate_vc not implemented\n");
}

void paxserver::become_underling(const viewid_t& newvid, node_id_t mgr,
                                 std::unique_ptr<view_t> newview) {
   MASSERT(0, "become_underling not implemented\n");
}

void paxserver::view_change_arg(const struct view_change_arg& vc_arg) {
   MASSERT(0, "view_change_arg not implemented\n");
}

void paxserver::view_change_reject(const struct view_change_reject& vc_rej) {
   MASSERT(0, "view_change_reject not implemented\n");
}

// One key question for creating new views is default include 
// or default exclude from Vold.  We default exclude
// Also, this allows views with only two servers
// Ends with manager accepting the new view
void paxserver::form_newview() {
   MASSERT(0, "form_newview not implemented\n");
}

// Manager
void paxserver::announce_newview() {
   MASSERT(0, "announce_newview not implemented\n");
}

void paxserver::view_change_accept(const struct view_change_accept& vc_acc) {
   MASSERT(0, "new_change_accept not implemented\n");
}


void paxserver::catchup_state(const view_t& view) {
   MASSERT(0, "catchup_state not implemented\n");
}

void paxserver::new_view_arg(const struct new_view_arg& nv_arg) {
   MASSERT(0, "new_view_arg not implemented\n");
}

void paxserver::new_view_res(const struct new_view_res& nv_res) {
   MASSERT(0, "new_view_res not implemented\n");
}


// Our protocol only requires transfer of paxobj
// This implies that our paxobj state is "manageably small", i.e., not a storage system
void paxserver::getstate_arg(const struct getstate_arg& gs_arg) {
   MASSERT(0, "getstate_arg not implemented\n");
}

void paxserver::getstate_res(struct getstate_res& gs_res) {
   MASSERT(0, "getstate_res not implemented\n");
}

void paxserver::init_view_request(const struct init_view_request& iv_req) {
   MASSERT(0, "init_view_request not implemented\n");
}

void paxserver::init_view_arg(const struct init_view_arg& iva) {
   MASSERT(0, "init_view_arg not implemented\n");
}
