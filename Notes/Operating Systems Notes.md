#### 3/19/14

### Kernel

[Wikipedia](http://en.wikipedia.org/wiki/Operating_system_kernel)

* A program that turns I/O requests from software into instructions for the CPU/memory/devices
* Programs (read *"processes"*) make *system calls*
    * **System call** -- request for resources or for operations to be performed by OS
        * It is the operating system's API
* Schedules which processes run when
* Decides which process has access to what memory locations when (via *virtual addressing*)
* Relays I/O requests from applications to the appropriate device (via *device drivers*)
    * **Device driver** -- provides an API for hardware devices
        * Hardware dependent, and operating system specific
        * Apple's open-source framework for developing OS X drivers is `I/O Kit`
* Methods for *synchronization* and *inter-process communication*
* Certain kernel functions can be delegated to special-purpose hardware
  (e.g. Memory Management Unit for checking access-rights for memory access)

#### Monolithic Kernel

* One single program that contains all of the code necessary to perform every kernel related task
* Traditionally used by *nix systems
* Allows the whole system to be much smaller
* More difficult development environment
* More difficult to maintain

#### Microkernel

* OS functionality is moved to a set of *servers* that comminicate with eachother via a "minimal kernel"
* First one designs a set of primitives (e.g. system calls for memory mgmt, scheduling, and IPC)
* Then higher low-level system implementations (e.g. networking) are user-space processes, called *servers*
* Easier to maintain and debug
* More context switches can slow down the system
* Bugs are less likely to bring the system down

### Concurrent Programming

Edsger Dijkstra proved ("Cooperating Sequential Processes", 1965) that from a
logical point of view, atomic lock and unlock operations operating on binary
semaphores are sufficient primitives to express any functionality of process
cooperation. [Wikipedia](http://en.wikipedia.org/wiki/Operating_system_kernel)

But that is such a painful method of programming, that we're still looking for
good abstractions and alternatives.

### Inter-Process Communication

**TODO**

[Wikipedia](http://en.wikipedia.org/wiki/Inter-process_communication)

Some Options:

* File
* Signal
* Socket
* Message queue
* Pipe
* Named pipe
* Semaphore
* Shared memory
* Message passing ("shared nothing")
* Memory-mapped file

