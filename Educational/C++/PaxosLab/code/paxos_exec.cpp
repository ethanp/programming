// Basic routines for Paxos implementation

#include "make_unique.h"
#include "paxmsg.h"
#include "paxserver.h"
#include "log.h"

void paxserver::execute_arg(const struct execute_arg& ex_arg) {
   MASSERT(0, "execute_arg not implemented\n");
}

void paxserver::replicate_arg(const struct replicate_arg& repl_arg) {
   MASSERT(0, "replicate_arg not implemented\n");
}

void paxserver::replicate_res(const struct replicate_res& repl_res) {
   MASSERT(0, "replicate_res not implemented\n");
}

void paxserver::accept_arg(const struct accept_arg& acc_arg) {
   MASSERT(0, "accept_arg not implemented\n");
}
