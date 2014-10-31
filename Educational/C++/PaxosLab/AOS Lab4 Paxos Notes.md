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

    ./pax -h # see how to configure

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
    
## The code as I see it

1. `tick_t` --- `uint64_t`; *time* in the simulation; `node.h`
2. `struct event`
    1. `tick` --- time associated with
    2. `nidid` --- node associated with
    3. `aid` --- type of event
        1. { die, sjoin, cjoin, pause, unpause }
3. `class Sched`
    4. `std::vector<event> events;` --- smallest tick value at the end
4. `class dssim_t`
    5. `tick_t ticks;` --- I *believe* this is the *current* tick number in the simulator?
2. The `main()` loop is `while(dssim.tick() || net.any_pending());`
    3. `bool dssim_t::tick()`
        1. `void dssim_t::do_events()`
            1. `while(config.sched.eventp(ticks))`
                1. `bool Sched::eventp(tick_t tick)`
                    1. `return 0` if there are no `event`s to process
                    2. Otherwise `return 1` *iff* the *earliest* event waiting to be processed is from before the passed-in `tick`
                2. 