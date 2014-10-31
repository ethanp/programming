
#include "node.h"

node_t::node_t(Net* _net, node_id_t mynid) :
   net(_net),
   nid(mynid),
   unpaused_tick(0)
{
}
node_t::~node_t() {}

node_id_t node_t::get_nid() const {
   return nid;
}

std::ostream& operator<<(std::ostream& os, const node_t& n) {
   n.pr_stat(os);
   return os;
}
