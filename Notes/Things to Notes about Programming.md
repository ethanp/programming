Unit testing
-------------------------------
**5/20/14**

[xUnit Wikipedia](http://en.wikipedia.org/wiki/XUnit)

* **Unit testing** -- testing the fundamental units of the software,
  checking outputs against expected outputs for given inputs

### xUnit architecture
* **xUnit** -- the collective name of frameworks for unit testing that use a
  particular popular architecture described by the collection of vocab terms herein

#### Test runner
Executable program that runs tests and reports their results

#### Test case
Elemental test case class

* A set of conditions or variables set up to determine whether some code is doing what it is supposed to
* Could be a requirement, a use case, a heuristic, etc.

#### Text fixture (context)
Preconditions (state) needed to run the test repeatable/consistently

* Loading a database with a specific, known set of data
* Creation of fake/mock objects

#### Test suite
A set of tests that all share the same fixture.
The order of the tests shouldn't matter.

#### Test execution
Each individual unit test is run in the following way

	setup(); // create text fixture
	execute test case
	teardown(): // destroy fixture to avoid disturbing other tests
	
#### Results formatter
Can output *plain text* or *XML* to integrate with build tools like Jenkins

#### Assertions
Expresses a logical condition that must be true in a correct environment.

### Other imortant vocab
[Javaranch - Evil unit testing](http://www.javaranch.com/unit-testing.jsp)

* **Regression suite** -- collection of tests that can all be run at once; could be unit or functional tests
* **Functional test** -- bigger than unit, smaller than component test
	* Exercises several methods/functions/classes working together
	* Allowed to take much longer than unit tests (which should be blazing fast)
* **Integration test** -- testing two+ components working together
* **Component test** -- running one component (defined by the application) by itself
* **System test** -- all components run together, as would happen in a normal usage scenario
* **Stress test** -- go bonkers; try to test concurrent code.
* **Mock** -- a fake version of an object that would have had to be used to test another object

### Sample opinions on unit testing

* "Usually, for every pound of production code, I would like to see about two pounds of unit
  tests and two ounces of functional tests (a little bit goes a long ways).
  The problem I see in too many shops is zero unit tests and a pound of functional tests." (Javaranch)

The correct type to use for currency values is java.math.BigDecimal
-------------------------------------------------------------------

Don't use `doubles`

Heap
----

#### 5/4/14

[Wikipedia](http://en.wikipedia.org/wiki/Heap_(data_structure))

* a binary heap is a **complete** binary tree which satisfies the heap ordering property.
* The ordering can be one of two types:
    1. the **min-heap** property: the value of each node is *greater than or equal* to
       the value of its parent, with the minimum-value element at the root.
    2. the **max-heap** property: same but flipped
* A heap is not a sorted structure but can be regarded as **partially ordered**.
    * There is no particular relationship among nodes on any given level, even among the siblings
* The heap is one maximally efficient implementation of a **priority queue**

Bloom Filters
-------------

#### 5/4/14

[i-programmer](http://www.i-programmer.info/programming/theory/2404-the-bloom-filter.html)

### The Point

* **Bloom filters *attempt* to tell you if you have seen a particular data item before**
* **False-positives** are ***possible***, **false-negatives** are ***not***

#### Approximate answers are faster

* You can usually trade space for time; the more storage you can throw at a problem the faster you can make it run.
* In general you can also trade certainty for time.

### Applications

* *Google's BigTable database* uses one to reduce lookups for data rows that haven't been stored.
* The *Squid proxy server* uses one to avoid looking up things that aren't in the cache and so on

### History

* Invented in 1970 by Burton *Bloom*

### How it works

* Uses multiple different hash functions in-concert

#### Initialization

* A Bloom filter starts off with a **bit array** `Bloom[i]` initialized to zero.

#### Insertion

* To record each data value you simply compute *k* different hash functions
* Treat the resulting *k* values as indices into the array and set each of the *k* array elements to `1`.

#### Lookup

* Given an item to look up, apply the *k* hash functions and look up the indicated array elements.
* If any of them are zero you can be 100% sure that you have never encountered the item before.
* *However* even if all of them are one then you can't conclude that you have seen the data item before.
    * All you can conclude is that it is *likely* that you have encountered the data item before.
    
#### Removal

* **It is impossible to remove an item from a Bloom filter.**


### Characteristics

* As the bit array fills up the probability of a false positive increases
* There is a formula for calculating the optimal number of hash functions to use for a given size of the bit array and the number of items you plan to store in there
    * k_opt = 0.7(m/n)
* There is another formula for calculating the size to use for a given desired probability of error and fixed number of items using the optimal number of hash functions from above
    * m = -2n * ln(p)


Redis
-----

#### 3/23/14

[StackOverflow](http://stackoverflow.com/questions/7888880/what-is-redis-and-what-do-i-use-it-for)

* [***RE**mote **DI**ctionary **S**erver*](http://redis.io/topics/faq)
* **NoSQL** key-value data store
* Data structure server
* Similar to **Memcached**, but with built-in *persistence* and more datatypes
* **Persistence** -- *snapshotting*, or *journaling* to disk
* **Datatypes** -- *Dictionary*, *List*, *(Sorted) Set*
* **Pub/Sub** transactions (see glossary at bottom)
* **Optimistic locking** -- (see glossary at bottom)
* The entire data set is stored in-memory (like Memcached)

##### Use Cases:

* Highly scalable data store shared by multiple processes, applications, or servers
* Caching layer



On the Method of Understanding a Programme
------------------------------------------

#### 3/13/14

* Run the program in a debugger
* Put breakpoints everywhere
* When it catches the first one, walk up the stack
* Put a breakpoint at the top of that stack
* Now restart from that outermost function and watch the thing unfold


Reactive Applications
---------------------
#### 2/23/14

According to [Typesafe](http://typesafe.com/platform)

Reactive applications have one or more of the following **defining traits:**

* **Event driven** -- enables parallel, asynchronous processing of messages or events with ease.
* **Scalable** -- across nodes elastically
* **Resilient** -- recovers and repairs automatically
* **Responsive** -- single-page UIs that provide instant feedback

In particular, the **Typesafe Reactive Platform** consists of the following **stack:**

* **Play!** -- Web Framework
* **Akka** -- Actor Model concurrency library
* **Scala** -- Programming Language
* **Typesafe Console** -- Console


#### 3/23/14

The best description of what it really means, I've found to be this
[lecture on Vimeo by Sadek Drobi](http://vimeo.com/48328895). He describes it
as being a pattern in which you do all your computations on `Promises`, and then
the whole string of events that will happen once you obtain your `Promise` is computed
and then the actual computation takes place whenever you do obtain the `Promise`.
But the point is that you're never waiting for the `Promise`, you just "react" by
computing it when you *do* receive it. Something like that, anyway.


Regex --- Lookahead & Lookbehind = "Lookaround"
-----------------------------------------------
#### 2/17/14

From [Regular-Expresions.info](http://www.regular-expressions.info/lookaround.html)

### Lookahead
* **Negative lookahead** --- match something *not* followed by something else
    * E.g. `q` *not* followed by `u` --- `q(?!u)`
* **Positive lookahead** --- match something *only if* it's followed by something else
    * E.g. `q` followed by `u` --- `q(?=u)`
        * Recall that the `u` is not consumed in this case
* Any valid regular expression can be used inside the lookahead
  (even capture groups, which do require their own set of parentheses within the lookahead)

### Lookbehind
* **Negative lookbehind** --- match something *not* preceded by something else
    * E.g. `b` *not* preceded by `a` --- `(?<!a)b`
* **Positive lookbehind** --- match something *only if* it's preceded by something else
    * E.g. `:` preceded by `cite` --- `(?<=cite):`
* Lookbehinds are generally restricted to only allowing some subset of the normal
  regex vocabulary, but the specifics vary (quite a bit) by language

### Notes:
* **They do not consume characters** in the string, they only assert whether a match is possible or not
* Some regexes would be impossible without them

Newlines
--------
#### 2/16/14

* On **Windows**, they use carriage-return & line-feed (`"\r\n"`)
* On **UNIX/Mac**, they use new-line, which is represented by the same
  ascii code as line-feed (`'\n'`), but does both CR-LF in one go

ASCII vs. UTF-8
---------------
#### 2/16/14

Both use the same number of bits, but ASCII is faster to read because it is not
a variable-width encoding, so the interpreter doesn't need to check the width
of each character.  UTF-8 is fully backwards-compatible to ASCII and avoids
endianness complications.  It seems to me, basically one should always use
UTF-8 instead of ASCII, and that is generally what people do.

FTP vs. HTTP
------------
#### 2/16/14

Mainly from [AlBlue's Blog](http://alblue.bandlem.com/2009/02/why-do-people-still-use-ftp.html)

* FTP was created for transferring files
    * That's why it gives you options for binary or ASCII modes
* HTTP was created for transmitting HTML
    * The file type is guessed based on the extension, though it can be specified
      in the header
* Originally, FTP was better for file downloads because HTTP/1.0 didn't
  support resumable downloads (where client disconnects part-way through and
  needs to restart). However, this was made possible in HTTP/1.1 via headers
  `Accept-Ranges` and `Content-Range`.
* HTTP also supports automatic data-compression, querying data-type before downloading,
  proxy support, running over SSL with HTTPS
* **People only use FTP over HTTP out of ignorance**
* In particular, WebDAV -- an extension of HTTP -- allows collaborative editing
  and management of documents stored on Web servers.

Glossary
--------

* **Reentrant** -- 2/16/14 -- a function that can be interrupted in the middle of its
  execution and then safely called again ("re-entered") before its previous
  invocations complete execution. Once the reentered invocation completes, the
  previous invocations will resume correct execution.
* **[Partial Function](http://en.wikipedia.org/wiki/Partial_function)** -- 3/1/14 --
  *normally* a function maps an *entire* domain to some range. In a **Partial Function**,
  however, not *every* element of the domain must be mapped. Some values in the domain may
  be *undefined* after passing * through the partial function.
* **[Optimistic Locking](http://en.wikipedia.org/wiki/Optimistic_locking)** -- 3/23/14 -- Before
  committing, each transaction verifies that no other transaction has modified the data it has
  read. If the check reveals conflicting modifications, the committing transaction rolls back
  and can be restarted.
* **[Messanging pattern](http://en.wikipedia.org/wiki/Messaging_pattern)** -- 3/23/14 --
  describes how two different parts of a message passing system connect and communicate
  with each other.
    * E.g. HTTP is a *request-response* pattern, UDP is a *one-way* pattern.
    * **Request-response** -- requester sends request message, replier receives, processes,
      and responds.
    * **Publish-subscribe** -- *publishers* post to an *intermediary message broker*, and
      *subscribers* register subscriptions with the broker. The broker might perform a
      *store and forward* to route messages to their destinations, and may prioritize the
      orderings.
        * E.g. RSS feeds
        * Provides better scalability than *request-response*