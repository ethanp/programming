// Basic routines for Paxos implementation

#include "make_unique.h"
#include "dssim.h"
#include "paxmsg.h"

////////////////////////////////////////////////////
// Basic operations
bool operator <(const viewid_t& x, const viewid_t& y) {
   return std::tie(x.counter, x.manager) < std::tie(y.counter, y.manager);
}
bool operator <=(const viewid_t& x, const viewid_t& y) {
   return std::tie(x.counter, x.manager) <= std::tie(y.counter, y.manager);
}
bool operator >(const viewid_t& x, const viewid_t& y) {
   return std::tie(x.counter, x.manager) > std::tie(y.counter, y.manager);
}
bool operator >=(const viewid_t& x, const viewid_t& y) {
   return std::tie(x.counter, x.manager) >= std::tie(y.counter, y.manager);
}

bool operator <(const viewstamp_t& x, const viewstamp_t& y) {
   return std::tie(x.vid, x.ts) < std::tie(y.vid, y.ts);
}
bool operator <=(const viewstamp_t& x, const viewstamp_t& y) {
   return std::tie(x.vid, x.ts) <= std::tie(y.vid, y.ts);
}
bool operator >(const viewstamp_t& x, const viewstamp_t& y) {
   return std::tie(x.vid, x.ts) > std::tie(y.vid, y.ts);
}
bool operator >=(const viewstamp_t& x, const viewstamp_t& y) {
   return std::tie(x.vid, x.ts) >= std::tie(y.vid, y.ts);
}

std::unordered_map<int, std::string> paxrpc2str = {
   {execute_arg::ID,       execute_arg::_descr},
   {replicate_arg::ID,     replicate_arg::_descr},
   {replicate_res::ID,     replicate_res::_descr},
   {accept_arg::ID,        accept_arg::_descr},
   {execute_success::ID,   execute_success::_descr},
   {execute_fail::ID,      execute_fail::_descr},
   {view_change_arg::ID,   view_change_arg::_descr},
   {view_change_reject::ID,view_change_reject::_descr},
   {view_change_accept::ID,view_change_accept::_descr},
   {new_view_arg::ID,      new_view_arg::_descr},
   {new_view_res::ID,      new_view_res::_descr},
   {init_view_arg::ID,     init_view_arg::_descr},
   {init_view_request::ID, init_view_request::_descr},
   {getstate_arg::ID,      getstate_arg::_descr},
   {getstate_res::ID,      getstate_res::_descr},
};

  

