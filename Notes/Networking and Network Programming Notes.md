## REST

##### 3/30/14

[Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content, expires 8/10/14](http://tools.ietf.org/pdf/draft-ietf-httpbis-p2-semantics-26.pdf)

HTML forms (up to and including HTML 5) can only send `GET` and `POST`.

### POST

Used to **modify and update a resource** (*not create*).

The `POST` method requests that the target resource process the 
representation enclosed in the request according to the resource’s
own specific semantics. For example, `POST` is used for the following
functions (among others):

* **Providing a block of data**, such as the fields entered into an HTML
  form, to a data-handling process;
* **Posting a message to a bulletin board**, newsgroup, mailing list,
  blog, or similar group of articles;
* **Creating a new resource** that has yet to be identified by the
  origin server; and
* **Appending data to a resource**’s existing representation(s).

#### If your action is not idempotent, then you *MUST* use `POST`

If you don't, you're just asking for trouble down the line.  `GET`, `PUT` and
`DELETE` methods are required to be idempotent. Imagine what would happen in
your application if the client was pre-fetching every possible `GET` request
for your service -- if this would cause side effects visible to the client,
then something's wrong. **This is true even if you're just sending a query
string with no body.**

### PUT

Used to **create a resource, or overwrite it**. While you specify the resources new URL.

The `PUT` method requests that the state of the target resource be
created or replaced with the state defined by the representation
enclosed in the request message payload.

The `PUT` method requests that the state of the target resource be created or
replaced with the state defined by the representation enclosed in the request
message payload. A successful `PUT` of a given representation would suggest that
a subsequent `GET` on that same target resource will result in an equivalent
representation being sent in a `200 (OK)` response. However, there is no
guarantee that such a state change will be observable, since the target
resource might be acted upon by other user agents in parallel, or might be
subject to dynamic processing by the origin server, before any subsequent `GET`
is received.

`PUT` is **idempotent**, so if you `PUT` an object twice, it has no effect [the second time].
This is a nice property, so I would use `PUT` when possible.
I think one cannot stress enough the fact that `PUT` is idempotent: 
if the network is botched and the client is not sure whether his request made it through,
it can just send it a second (or 100th) time, and it is guaranteed by the HTTP spec that
this has exactly the same effect as sending once.

It must be stressed that `PUT` is *defined* to be idempotent, but you still have to
write your server in such a way that `PUT` behaves correctly.

## [SOAP](http://en.wikipedia.org/wiki/SOAP)

### TODO

### Simple Object Access Protocol

Old school web-interface pattern that uses verbose XML.
That's what I seem to remember it being anyway.

## [Apples Networking Concepts](https://developer.apple.com/library/ios/documentation/NetworkingInternet/Conceptual/NetworkingConcepts/NetworkingTerminology/NetworkingTerminology.html)

##### 3/15/14

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

## [Apples Networking Overview](https://developer.apple.com/library/ios/documentation/NetworkingInternetWeb/Conceptual/NetworkingOverview/Introduction/Introduction.html)

##### 3/15/14

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

To avoid *latency* issues whenever your program needs to send multiple messages
(resource requests, acknowledgments, and so on) that are not dependent on one
another, send them all simultaneously rather than waiting for a response to one
message before sending the next.

##### Cache Resources Locally

Only redownload the thing if you know it has changed.

##### TLS (Transport Layer Security)

TLS is the successor to the SSL protocol. In addition to encrypting data over
the wire, TLS authenticates a server with a certificate to prevent spoofing.



### OSI Layers

##### 3/7/14

1. **Physical**
2. **Data Link**
3. **Network**
4. **Transport**
5. **Session**
6. **Presentation**
7. **Application**

### TCP/IP Layers

##### 3/7/14

1. **Network Interface** -- (OSI 1,2) -- uses the hardware to do the above layers
    * TCP/IP is designed to be hardware independent
2. **Internet** -- (OSI 3) -- congestion control and routing global unique IP addresses and IP packets
3. **Transport** -- (OSI 4) -- error control, segmentation, congestion control, application addresssing
4. **Application** -- (OSI 5,6,7) -- protocols used by applications
    * **FTP** -- for transmitting files, out-of-date (see below)
    * **SMTP** (Simple Mail Transfer Protocol) -- for sending mail between mail servers
    * **HTTP** -- used by the WWW
    * **POP3** (Post Office Protocol Version 3) -- for retrieving email from mail server
    * **IMAP** (Internet Message Access Protocol) -- for retrieving email from mail server
    * **DNS** (Domain Name System) -- for translating name to an IP address
    * **TCP** (Transmission Control Protocol) -- creates *reliable* connection between 2 computers
    * **IP** (Internet Protocol) -- addresses hosts and routes packets across 1+ IP networks
    * **ICMP** (Internet Control Message Protocol) -- provides error messages
    * **UDP** (User Datagram Protocol) -- *broadcasts* messages over a network

