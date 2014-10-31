latex input:        mmd-article-header
Title:              Lab #4 Paxos: Notes
Author:             Ethan C. Petuchowski
Base Header Level:  1
latex mode:         memoir
Keywords:           Operating Systems, C, ELF, Linux
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

## Summary
> ***You will implement the Paxos replication algorithm within the context of a distributed systems simulator.***

**You are *only allowed to modify* `paxos_exec.cpp`.** Make sure of this before turning the lab in, because *all other fils* will be overwritten by their tests of my work.


### Compile and Run

    make pax

    pax -h # see how to configure
    
    pax -fl DEBUG # run with a ton of prints

Since we're not dealing with *view changes*, we need to form an initial view using a "fake initial view change" with the `-f` option

### Interface

1. `class dssim_t.[cpp|h]` --- distributed system simulator framework
2. `class Net.[cpp|h]` --- network code
3. `pax[server|client].[cpp|h]` --- servers and clients
    1. **Client** --- generate work
    2. **Server** --- respond to messages
4. `paxtypes.h` --- basic types
5. `paxmsg.h` --- message types
6. `class Paxlog.[cpp|h]` --- the Paxos **log**
    1. Treat it as persistent
    2. `Paxlog::trim_front` --- erase executed entries
    3. `bool Paxlog::next_to_exec(it)` --- determine if an entry is next to execute
7. `class paxobj.[cpp|h]` --- base class for servers' underlying state machine
    1. **Request()**
        1. Changes `paxobj` state
        2. Returns a string
        3. Server logs request
        4. Server executes log entries when appropriate using `paxserver::paxop_on_paxobj` ("tell Paxos Server to perform operation on object")

#### Where I come in

1. **Implement** handlers for servers in `paxos_exec.cpp`
    1. This does *not* include view change (the bulk of the paper...)
2. Don't worry about persistence

## Some common shorthands
1. `vs` --- **viewset**
2. `it` --- **iterator** object
3. `rid` --- **request id**
4. `tup` (for the `Paxlog`) --- this is what the *log* is composed of
    1. Implemented as an inner struct in Paxlog
    
## What I See

### The Logs
1. `C04 primary:1` --- `paxclient.cpp:38 paxclient::paxclient` --- the `paxclient` constructor is successfully initializing a client
    1. It `assert`s that servers have already been initialized at this point
    2. The `04` is this client's `node_id_t nid` (inherited from `node_t` in `node.h`)
    3. The `1` is the randomly assigned `node_id_t primary` server
2. `C06 new work:c-aa rid:1` --- `word_vec_pax.cpp:82 std::unique_ptr<paxclient::req_cb> pc_word_vec::work_get()`
    
    
### The Types
1. `tick_t` --- `uint64_t`; *time* in the simulation; in `node.h`
2. `rid_t` --- `uint64_t`; *request id*; in `paxtypes.h`
3. `cb` --- `std::unique_ptr<std::function<void(std::string)>>`; pointer to a function that takes a string and returns nothing; typedef'd in `paxclient.h`
2. `struct event`
    1. `tick` --- time associated with
    2. `nidid` --- node associated with
    3. `aid` --- type of event
        1. { die, sjoin, cjoin, pause, unpause }
3. `class Sched`
    4. `std::vector<event> events;` --- smallest tick value at the end
4. `class dssim_t` --- "distributed system simulator"
    5. `tick_t ticks;` --- I *believe* this is the *current* tick number in the simulator?
3. A **client** is of type `paxclient`, which is a ("public") *subtype* of `node_t`
    1. it has a `rid_t local_rid` --- request counter
4. A **server** is similar
    5. telling a server to `bool tick() = false;`
5. `class po_word_vec : public paxobj  [word_vec_pax.h]` --- **todo** (I got here from log-line-2, which was in word_vec_pax which used this thing)
6. `class paxobj  [paxobj.h]` --- the base class of internal state machines (as noted above)
    1. **todo**
7. `class pc_word_vec : public paxclient  [word_vec_pax.h]` --- the client *implementation* used when you just run the thing
    1. `std::vector<std::string> wvec =  "{ [prefix]-a", "[prefix]-b", ..., "...-z" };`
    2. `std::string prefix;` --- a lowercase-letter    
8. `struct paxclient::req_cb` --- "request callback"
    1. `paxobj::op request`
    2. `cb reply_cb` ("callback") --- pointer to a `function<void(string)>`
9. `struct paxobj::op  [paxobj.h]` --- essentially a named function that takes a state machine pointer and returns a string
    1. `std::function<std::string (paxobj*)> func;` --- a function that takes a state machine pointer and returns a string
    2. `std::string name;` --- the name of this `op`
9. `request` --- "a function that takes the paxos object `[paxobj]`, changes its state, and, and returns a `string
    1. physically, it's a pointer to an `op`

### The Code

1. The `main()` loop is `while(dssim.tick() || net.any_pending());`
2. `bool dssim_t::tick()`
3. `void dssim_t::do_events()`
    1. `while(config.sched.eventp(ticks))` --- while there is an event from before now to process
    2. `bool Sched::eventp(tick_t tick)`
        1. `return 0` if there are no `event`s to process
        2. Otherwise `return 1` *iff* the *earliest* event waiting to be processed is from before the passed-in `tick`
4. I
                
## The Code

### C++
1. [`static_cast<T>(value)`][sc] --- convert one type into another
    
        double result = static_cast<double>(4)/5;
1. [`std::shared_ptr<T>`][sp] --- does **ref-count garbage collection** on our behalf, so we don't need to deallocate manually
2. [`std::make_unique<T>()`][mu] --- call's `T`'s constructor and wraps the object in a `std::unique_ptr`
3. [`std::unique_ptr`][up] --- the sole owner of the thing pointed to, `delete`'s the object pointed to when the `unique_ptr` goes out *of scope*, 
    1. Reasons delete function will be called:
        1. `unique_ptr` is explicitly destroyed
        2. `unique_ptr` is reassigned via `operator=` or `reset()`
    2. You can specify your own `Deleter` of `T`s for each `unique_ptr`
4. [`std::move`][mv] --- turn an rvalue into an lvalue (really an rvalue reference) so we can invoke the move constructor
    1. *Move semantics* --- avoid unnecessary copying of temporary objects
    2. Relies on C++11's "rvalue references"
    3. *lvalue* --- has a memory address
    4. *rvalue* --- is a temporary value with no address
        1. E.g. when you *return by value*, that thing you return is an *rvalue*, which means the entire functions acts as an *rvalue*
    5. *rvalue reference* --- can only bind to an *rvalue* (rvalue references are actually *lvalue*s)
        1. *Syntax:* --- "double ampersand": `string&& rVRef = getName();`
    6. *Move constructor* --- like a copy constructor, creates a new instance based on a passed-in rvalue reference more efficiently
        1. This process can be optimized compared to a copy constructor because we know the rvalue reference we're copying from won't ever be needed later or modified
        1. For primitive types we copy as usual
        2. For pointers, we can *steal* the pointer and *null out* the original!
        3. The move constructor only called for temporary objects that can be modified
            4. A function that returns a `const` object, will cause the copy constructor to run instead
2. [`std::function<T(U,...)>`][fctn] --- can store, copy, and invoke any `Callable` target (e.g. functions and lambdas)
    1. `T` is the return type
    2. `U,...` are the argument types
    3. So for example (tested!)
            
            long a(int d) { return d*2; }
            std::function<long(int)> v = a;
            std::cout << v(3) << '\n'; // => 6
        
3. [*Lambdas*][lbd] --- "Constructs a closure: an unnamed function object capable of capturing variables in scope."
    1. `[ capture_list ] ( params ) -> return_type { function_body }`
    2. *Capture list* options
        1. `[=]` --- captures all automatic (stack-local) variables in `{ function_body }` *by value*
        
2. The nastiest-looking line of code I have ever seen (`word_vec_pax.cpp:83 std::unique_ptr<paxclient::req_cb> pc_word_vec::work_get()`
        
        /**
         we're creating a "req_cb" request callback object, which requires two objects:
         
            1. an "op", which has
                1. a function that
                    1. takes a po_word_vec and captures the
                        current work_word (the next string [e.g. "prefix-a"] in this pc_word_vec)
                    2. pushes the work_word onto the po_word_vec
                    3. returns the given work_word
                2. a name (the work_word)
            
            2. a "cb" ("callback") which is a function that takes a string and returns void.
               In our case, what it does is:
                1. Takes the word passed as a reply (string reply_word)
                2. Makes sure it is the same word given
                3. Increments the (state) callback-count
         
         then we return this request callback created above
         */
         
        auto res = std::make_unique<paxclient::req_cb>(
            std::make_unique<paxobj::op> (
                [=](paxobj* _local_this) -> std::string {
                    auto local_this = static_cast<po_word_vec*>(_local_this);
                    local_this->push_back(work_word);
                    return work_word;
                }, work_word),
            std::make_unique<std::function<void(std::string)>>(
                [=](std::string reply_word) {
                    cb_cnt++;
                    if(reply_word != work_word) {
                        LOG(l::WARN,  "Yikes: " << id_str()
                            << " work_word: " << work_word
                            << " reply: " << reply_word
                            << std::endl);
                    }
                }
            )
        );

[sp]: http://en.cppreference.com/w/cpp/memory/shared_ptr
[fctn]: http://en.cppreference.com/w/cpp/utility/functional/function
[lbd]: http://en.cppreference.com/w/cpp/language/lambda
[mu]: http://en.cppreference.com/w/cpp/memory/unique_ptr/make_unique
[up]: http://en.cppreference.com/w/cpp/memory/unique_ptr
[mv]: http://www.cprogramming.com/c++11/rvalue-references-and-move-semantics-in-c++11.html
[sc]: http://www.cprogramming.com/reference/typecasting/staticcast.html

### Nice Tricks
1. Make a switch to disable a big block of code

        #if 0
        all this = code(is);
        now disabled;
        enable with = set_if(1);
        #endif