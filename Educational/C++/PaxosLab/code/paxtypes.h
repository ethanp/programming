// Definitions for some basic types used in Paxos
#pragma once

#include <set>
#include <iostream>

// Request type, viewid, viewstamp, view

// Declarations mostly come from and only lightly modified from 
//   Mazieres,  "Paxos Made Practical"
// Group identifier (id_t in Mazieres) eliminated
// cid (cohort identifier) and net_address_t are a tuple in
// Mazieres that identifies a Paxos participant and allows
// network communication.  Our node_id_t (node.h)
//  serves both those functions.
typedef uint64_t rid_t; // Request identifier, 0 is illegal

struct viewid_t {
   uint64_t counter;
   node_id_t manager; // node_id_t instead of cid
   bool operator ==(const viewid_t& vid) const {
      return (counter == vid.counter) && (manager == vid.manager);
   }
   bool operator !=(const viewid_t& vid) const {
      return (counter != vid.counter) || (manager != vid.manager);
   }
};
bool operator <(const viewid_t& x, const viewid_t& y);
bool operator <=(const viewid_t& x, const viewid_t& y);
bool operator >(const viewid_t& x, const viewid_t& y);
bool operator >=(const viewid_t& x, const viewid_t& y);
std::ostream& operator<<(std::ostream& os, const viewid_t&);

namespace std {
   template<>
      struct hash<viewid_t> {
      typedef viewid_t argument_type;
      typedef std::size_t value_type;
 
      value_type operator()(const argument_type& s) const {
         value_type const h1 ( std::hash<uint64_t>()(s.counter) );
         value_type const h2 ( std::hash<node_id_t>()(s.manager) );
         return h1 ^ (h2 << 12);
      }
   };
}

struct viewstamp_t {
	viewid_t vid;
	uint64_t ts;
   bool operator==(const viewstamp_t& vs) const {
      return (vid == vs.vid) && (ts == vs.ts);
   }
   bool operator!=(const viewstamp_t& vs) const {
      return !(*this == vs);
   }
   bool sucessor(const viewstamp_t& vs) const {
      // Within a view, timestamps increase by 1
      if(vid == vs.vid) return (ts == (vs.ts+1));
      // Across views, timestamps begin with 1
      return (vid > vs.vid) && (ts == 1ULL);
   }
};
std::ostream& operator<<(std::ostream& os, const viewstamp_t& vs);
bool operator <(const viewstamp_t& x, const viewstamp_t& y);
bool operator <=(const viewstamp_t& x, const viewstamp_t& y);
bool operator >(const viewstamp_t& x, const viewstamp_t& y);
bool operator >=(const viewstamp_t& x, const viewstamp_t& y);


namespace std {
   template<>
      struct hash<viewstamp_t> {
      typedef viewstamp_t argument_type;
      typedef std::size_t value_type;
 
      value_type operator()(const argument_type& s) const {
         value_type const h1 ( std::hash<viewid_t>()(s.vid) );
         value_type const h2 ( std::hash<uint64_t>()(s.ts) );
         return (h1<<12) ^ (h2);
      }
   };
}

struct view_t {
   viewid_t vid;
   node_id_t primary;
   // Not really sure if this should be a set, it is for set_difference
   std::set<node_id_t> backups;
   bool elt_in(node_id_t node) const {
      if(node == primary) return true;
      for(const auto& backup : backups ) {
         if( backup == node ) return true;
      }
      return false;
   }
   std::set<node_id_t> get_servers() const {
      std::set<node_id_t> s = backups;
      s.insert(primary);
      return s;
   }
   bool operator==(const view_t& v) {
      return (vid == v.vid) 
      && (primary == v.primary)
      && (backups == v.backups);
   }
};
std::ostream& operator<<(std::ostream& os, const view_t& vid);

