#include <unordered_map>
#include <stdint.h>
#include <iostream>
#include <algorithm>
#include <iterator>
#include <memory>
#include "make_unique.h"

struct B {
   B(){}
};
struct A {
   A(std::unique_ptr<B> _b) :
      b(std::move(_b))
   {
   }
   std::unique_ptr<B> b;
};

int main() {
   for(int i = 0; i < 1000; ++i) {
      std::make_unique<A>(std::make_unique<B>());
   }
   return 0;
}

#if 0

/////////////////////////////////////////////////////

typedef int node_id_t;
typedef uint64_t rid_t; // Request identifier, 0 is illegal

std::unordered_multimap<node_id_t,rid_t> exec_rid_cache;
// While it would probably be better to keep timestamps with
// our rid cache and trim the cache periodically, we just keep
// the last 500 request IDs from each client.
// Note that even with only a single outstanding request per client
// because of timeouts, we can have multiple requests per client.
const unsigned int max_rid_in_cache = 500;

void ins(node_id_t nid, rid_t rid) {
   exec_rid_cache.insert({nid, rid});
   // Erase oldest entries until we fall below max_rid_in_cache
   while(exec_rid_cache.count(nid) > max_rid_in_cache) {
      auto range = exec_rid_cache.equal_range(nid);
      std::advance(range.first, max_rid_in_cache);
      exec_rid_cache.erase(range.first);
      //exec_rid_cache.erase(std::prev(range.second));
   }
}

int
main() {
   for(int i = 0; i < 2000; ++i) {
      ins(7, i);
   }
   for(int i = 0; i < 2000; ++i) {
      ins(8, i);
   }
   auto range = exec_rid_cache.equal_range(8);
   std::for_each(range.first, range.second,
                 [&](std::unordered_multimap<node_id_t,rid_t>::value_type& x){
                    std::cerr << x.second << " ";
                 });
   std::cerr << "\n";
   std::cerr << "size(7) " << exec_rid_cache.count(7)
             << " size(8) " << exec_rid_cache.count(8)
             << "\n";
   return 0;
}
///////////////////////////////////////////////////////////
#include <stdio.h>

struct A {
  int foo;
};

struct B : public A {
  int bar;
};

struct C : public B {
  int quux;
};

int main () {
  struct C c;
  printf("sz a:%d b:%d c:%d offset a:%d b:%d c%d\n",
         sizeof(A), sizeof(B), sizeof(C),
         ((char*)&c.foo - (char*)&c),
         ((char*)&c.bar - (char*)&c),
         ((char*)&c.quux - (char*)&c));
  return 0;
}
#endif
