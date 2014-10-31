// node.h - generic node in a distributed system
#pragma once
#include <stdint.h> 
#include <queue>
#include <iostream>
#include "massert.h"

// This is a combination cohort ID and net_address_t.  You can use it
// to communicate with a paxos node, and if a paxos node really
// crashes (i.e., loses its disk state), then its node_id would change.
typedef int node_id_t;

// Time in the simulation
typedef uint64_t tick_t;

// Forward declaration
class Net;
class paxobj;

class node_t {
public:
   node_t(Net*, node_id_t);
   virtual ~node_t();
   // Time passes.  Clients return false when done, Servers always return false
   virtual bool tick() = 0;
   node_id_t get_nid() const;
   // A C string of the form S02 for nid 02 and a paxos server
   //  Here for convenience and efficiency
   virtual const char* id_str() const = 0;
   void set_unpaused_tick(tick_t now) {unpaused_tick = now;}

protected:
   Net* net;
   node_id_t nid;
   // If I pause, I want to know when I wake up, so I don't flood
   // my neighbors with "hey, you haven't spoke to me in forever" messages
   tick_t unpaused_tick;
   // Print stats, made public by operator<<
   virtual std::ostream& pr_stat(std::ostream&) const = 0;
   friend std::ostream& operator<<(std::ostream& os, const node_t& n);

private:
   // Convenience functions for the simulation
   friend class dssim_t;
   // server allows dssim::get_server, which is a way 
   // for the first client to find any server, e.g., www.microsoft.com
   virtual bool server() const { return false; }
   // primary allows correctness checking
   // Note, Paxos can be in a state where no node will return true for
   // primary.  There must be a functioning view for primary to return true
   virtual bool primary() const { return false; }
   // Query primary if given node ID is in view
   virtual bool in_view(node_id_t) const { MASSERT(0, "Not primary"); }
   // std::unique_ptr wasn't working, presumably b/c bad copy initialization.
   virtual paxobj* get_paxobj() { return nullptr; }
};
std::ostream& operator<<(std::ostream& os, const node_t& n);
