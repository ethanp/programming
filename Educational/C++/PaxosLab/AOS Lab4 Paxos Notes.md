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

## Implementing `paxos_exec.cpp`

### Methods

    void paxserver::execute_arg(const struct execute_arg& ex_arg)
    void paxserver::replicate_arg(const struct replicate_arg& repl_arg)
    void paxserver::replicate_res(const struct replicate_res& repl_res)
    void paxserver::accept_arg(const struct accept_arg& acc_arg)

### About

1. Execute arg is called by `void paxserver::dispatch(paxmsg_t &paxmsg)` when `paxmsg.rpc_id == execute_arg::ID`
2. Which is called by `bool paxserver::tick(void)`, where `paxmsg` is returned by `net->recv(paxserver)`
3. Which pops a message off the queue for this server which was place their by a client when it `tick`ed.

## Summary
> ***You will implement the Paxos replication algorithm within the context of a distributed systems simulator.***

**You are *only allowed to modify* `paxos_exec.cpp`.** Make sure of this before turning the lab in, because *all other fils* will be overwritten by their tests of my work.


### Compile and Run

    make [clean] pax

    pax -h # see how to configure
    
    pax -fl DEBUG # run with a ton of prints
    
    # debug
    gdb
    file pax
    run -fl DEBUG --delay 0 --shuffle 0

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
    1. This does *not* include view change (the bulk of the paper [and according to Witchel the main part of the algorithm...] ...)
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
    1. the `main` loop has `tick`ed
    2. the `sched` finished executing all the `event`s
    3. the `dssim` is `tick`ing each of the `node`s
    4. so it's calling `bool paxclient::tick(void)`
    5. which calls `net->recv(this_client)`
        1. which finds an `inq` for this node (Client 6) in the `net`
        2. but it has no `net_msg`s (I'm not sure here), so it returns a `nullptr`
    5. but `!this_client->work_done()` indicates that `cb_cnt < max_req`
    1. so `this_client` is allowed to make a new request via `next_req_rec()`
    1. `paxclient::next_req_rec` calls `paxclient::req_cb pc_word_vec::work_get(void)`
    2. which (fully explained below) pulls the `client`'s next `work_word` out
        1. creates a a `paxobj::op std::function<void(string)>` that pushes it into a `po_word_vec`
        2. creates a callback that `cb_cnt++` and `assert reply_word == sent_word`
    1. Client 6 indicates that it sent `work_word == c-aa` and  by logging the above message.
    2. Back in `paxclient::next_req_rec`, it
        1. logs the `local_rid` (request counter) to be 1 (it *starts* at 1)
        2. creates & returns a `struct paxclient::req_rec` for the request created
    3. Back in `paxclient::tick`
    1. the returned `req_rec` is turned into a `struct execute_arg : paxmsg_t : net_msg_t`
    2. and passed to `bool net->send(primary)`
    3. which makes sure the destination is still a part of the network (i.e. stil *alive*)
        4. (in our case it's *not* dead, because it would have logged that that happened)
    4. fill in metadata of the `execute_arg` about this packet
    5. push the `execute_arg` onto `net::inqs`
    6. return to `paxclient::tick`
    7. insert the `req_rec` to `paxclient::out_req`
        1. which in the current implementation can only hold one outstanding `execute_request` at a time
    8. increment `struct paxclient::stat.started_op`
        1. wrt `struct paxclient::stat.success_op`, which is not incremented
    9. `paxclient::tick()` returns `true` to `bool dssim_t::tick()`
    10. which continues to loop through and `tick` all the `nodes`
3. **3** of the above messages are printed because there are 3 clients    
4. `Initial view ([cnt:1 mgr:3]  pr:3 bk: 1 2)` --- `paxserver.cpp:328 void paxserver::do_fake_init_vc()`
    1. I'll just start by describing `void paxserver::do_fake_init_vc()`
    2. create a `view_t` and configure it according to the algorithm
    3. Log the configured view (as we see above)
    4. That's it for that function, but "How did we *get* here?" you ask. From a `paxserver` getting `tick`ed, as I describe below when explaining the next 3 lines of log.
5. `S01 executeARG from C05 R(002)`
    5. The previous log triplets were from `client`s getting `tick`ed
    6. Well similarly, *this* triplet is from the `primary` `paxserver` getting `tick`ed
    7. The server notices that there is no `primary` in its own `vc_state` (view state)
    8. So it executes the `do_fake_init_vc()` described above
    9. then it procedes to process 3 messages
    10. it calls `net->recv(this)` to tell the `paxserver` to `pop_front` the next item in its `inq`
    11. it passes the `paxmsg_t` to `void paxserver::dispatch(paxmsg_t)`
    12. which ... **TODO (START HERE, BRAH)**
    
    
    
### The Types
1. `tick_t` --- `uint64_t`; *time* in the simulation; in `node.h`
2. `rid_t` --- `uint64_t`; *request id*; in `paxtypes.h`
3. `cb` --- `std::unique_ptr<std::function<void(std::string)>>`; pointer to a function that takes a string and returns nothing; typedef'd in `paxclient.h`
2. `struct event` --- { time, node, type }
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
5. `class po_word_vec : public paxobj  [word_vec_pax.h]` --- **todo** (I got here from log-line-2, which was in `word_vec_pax.cpp` which used this thing)
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
10. `struct viewid_t`
    1. `uint64_t counter`
    2. `node_id_t manager`
    3. `==` & `!=` operators
11. `struct viewstamp_t` --- a viewid and a timestamp, with a function for returning whether once `viewstamp_t` is the `successor` of another
    1. Used in `Paxlog::tup` which is a logged operation, sequentially ordered in the `Paxlog` by viewstamp
    2. The `Paxlog` itself is an alement of a `paxserver : node_t`
        1. Though I'm jumping the gun a bit because I haven't gotten here in the code.
10. `struct view_t  [paxtypes.h]` --- { counter int, manager node, primary node, backup node-set } 
    1. `viewid_t vid` --- counter int and a manager node
    2. `node_id_t primary` --- node ID of the primary for this view [duh!]
    3. `set<node_id_t> backups` --- I guess everyone who's *not* the `primary`

### Main

1. Set the network configuration variables using the given defaults & arguments
2. `dssim.configure`
    1. make `num_server` `paxservers` with `po_word_vec`s as `poxobj`s
        1. add them to the `nodes` map (`id -> node`) of the `dssim`
    2. make `num_client` `pc_word_vec`s, each with its own prefix, and an initialized word vector
        1. add these to the `nodes` map as well
3. The `main()` loop is `while(dssim.tick() || net.any_pending());`
2. `bool dssim_t::tick()`
3. `void dssim_t::do_events()`
    1. `while(config.sched.eventp(ticks))` --- while there is an event from before now to process
    2. `pop` the *earliest* `event` off the `sched`
    3. perform it
4. `tick()` every `node` that is not paused
    1. `void paxclient::tick(void)` --- **TODO**
                
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

#### Nice Tricks
1. Make a switch to disable a big block of code

        #if 0
        all this = code(is);
        now disabled;
        enable with = set_if(1);
        #endif
        
### CLion Hotkeys

| **action** | **keys** |
| :--------- | :------: |
| jump from declaration to implementation   | `ctrl+cmd+up`     |
| find usages                               | `ctrl+cmd+dwn`    |
| jump to class                             | `cmd+O`           |
| jump to symbol                            | `optn+cmd+O`      |
| jump to previous/next location            | `cmd+[`/`]`       |
