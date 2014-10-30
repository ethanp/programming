#pragma once
#include <functional>
#include <memory>

// A class must inherit from this base class to function in our paxos
// implementation.
class paxobj {
  public:
   paxobj() {}
   virtual std::unique_ptr<paxobj> clone() {return nullptr;}
   virtual ~paxobj() {}
   // A paxos operation
   struct op {
   op(std::function<std::string (paxobj*)> _func, const std::string& _name) :
      func(_func),
         name(_name) {
      }
      std::function<std::string (paxobj*)> func;
      std::string name;
   };
   // In our system, a paxos request is a function that takes
   // the paxos object, changes its state, and returns a string
   // Because I keep copies in the log and in local processing, this
   // is a shared_ptr
   typedef std::shared_ptr<op> request;
   // We pass execute the execute_arg::request (paxos operation) 
   // which is a lambda that transforms the object being paxos-ified
   // on the servers.
   // By using lambdas, we can do any instance operations and include
   // any needed data the closure.
   // The return type is a string, which is close enough to a binary blob
   std::string execute(request f) {
      return (f->func)(this);
   }
   // Not strictly necessary, but helpful for simulation to check 
   // its correctness.  Generic object can't be equal to anything
   virtual bool eq(const paxobj* o) const { return false; }
   virtual std::string to_str() const { 
      return "GENERIC paxobj (it is probably a mistake if you see this)"; }
};
// Implementation in paxos_print.cpp
std::ostream& operator<<(std::ostream& os, const paxobj& po);
