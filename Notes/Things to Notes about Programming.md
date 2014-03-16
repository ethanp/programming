
On the Method of Understanding a Programme
------------------------------------------

#### 3/13/14

* Run the program in a debugger
* Put breakpoints everywhere
* When it catches the first one, walk up the stack
* Put a breakpoint at the top of that stack
* Now restart from that outermost function and watch the thing unfold

Networking
----------

##### 3/15/14

[Apples Networking Concepts](https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/NetworkingConcepts/NetworkingTerminology/NetworkingTerminology.html)

* **Host** -- any device **provides an endpoint for networked communication**
    * It is called a host because it **hosts the applications and daemons that run on it**
    * E.g.s
        * desktop computer
        * server
        * iOS device
        * virtual machine running on a server
        * the VoIP telephone sitting on your desk

* **Packets** -- small pieces of the data to be sent
    * **Header** -- says **where** the packet should be sent
    * **Payload** -- the actual **data**
    * **Trailer** -- contains a **checksum** to ensure the packet was received correctly
        * Sometimes this is inside the header
    * **Encapsulation** -- often one packet contains another packet
        * E.g. an Ethernet packet might hold an entire TCP packet as its payload
* **Latency** -- **round-trip time** of a request (to and from the destination machine)
    * Satellite connections are "painfully" slow because of the distance that must be travelled

* **Firewall** -- any router that inspects traffic, modifies traffic, or blocks
  specific subsets of traffic that flows through it.

[Apples Networking Overview](https://developer.apple.com/library/ios/documentation/NetworkingInternetWeb/Conceptual/NetworkingOverview/Introduction/Introduction.html)

Higher-level APIs also solve many of the most complex and difficult networking
problems for you -- caching, proxies, choosing from among multiple IP addresses
for a host, and so on. If you write your own low-level code to perform the same
tasks, you have to handle that complexity yourself (and debug and maintain
the code in question).

Be wary of all incoming data. Any data received from an untrusted source may be
a malicious attack. Your app should carefully inspect incoming data and
immediately discard anything that looks suspicious.

Although your software cannot magically fix a truly broken network, poorly
written networking code can easily make things much, much worse. For example,
suppose a server is heavily overloaded and is taking 45 seconds to respond to
each request. If your software connects to that server with a 30-second
timeout, it contributes to the server’s workload, but never successfully
receives any data.

And even when the network is working perfectly, poorly written networking code
can cause problems for the user—poor battery life, poor performance, and so on.

##### Batch Your Transfers, and Idle Whenever Possible

When writing code in general, to the maximum extent possible, you should
perform as much work as you can and then return to an idle state. This applies
doubly for network activity. If you keep their radio on, you're wasting their
battery life.

##### Cache Resources Locally

Only redownload the thing if you know it has changed.

##### TLS (Transport Layer Security)

TLS is the successor to the SSL protocol. In addition to encrypting data over
the wire, TLS authenticates a server with a certificate to prevent spoofing.


##### 3/7/14

### OSI Layers

1. Physical
2. Data Link
3. Network
4. Transport
5. Session
6. Presentation
7. Application

### TCP/IP Layers

1. Network Interface -- (OSI 1,2) -- uses the hardware to do the above layers
    * TCP/IP is designed to be hardware independent
2. Internet -- (OSI 3) -- congestion control and routing global unique IP addresses and IP packets
3. Transport -- (OSI 4) -- error control, segmentation, congestion control, application addresssing
4. Application -- (OSI 5,6,7) -- protocols used by applications
    * FTP -- for transmitting files, out-of-date (see below)
    * SMTP (Simple Mail Transfer Protocol) -- for sending mail between mail servers
    * HTTP -- used by the WWW
    * POP3 (Post Office Protocol Version 3) -- for retrieving email from mail server
    * IMAP (Internet Message Access Protocol) -- for retrieving email from mail server
    * DNS (Domain Name System) -- for translating name to an IP address
    * TCP (Transmission Control Protocol) -- creates *reliable* connection between 2 computers
    * IP (Internet Protocol) -- addresses hosts and routes packets across 1+ IP networks
    * ICMP (Internet Control Message Protocol) -- provides error messages
    * UDP (User Datagram Protocol) -- *broadcasts* messages over a network

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
========

* **Reentrant** -- 2/16/14 -- a function that can be interrupted in the middle of its
  execution and then safely called again ("re-entered") before its previous
  invocations complete execution. Once the reentered invocation completes, the
  previous invocations will resume correct execution.
* **[Partial Function](http://en.wikipedia.org/wiki/Partial_function)** -- 3/1/14 -- *normally* a function maps an *entire* domain to some range. In a **Partial Function**, however, not *every* element of the domain must be mapped. Some values in the domain may be *undefined* after passing through the partial function.
