latex input:		mmd-article-header
Title:				Networking and Network Programming Notes
Author:			Ethan C. Petuchowski
Base Header Level:		2
latex mode:		memoir
Keywords:			REST protocol, protocols, sockets, TCP/IP
CSS:				http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:		2014 Ethan C. Petuchowski
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

## Socket

### Socket Vs Port
**6/18/14**

* From [StackOverflow][S vs P]
* A **TCP Socket** is an `(IP-Address) + (Port Number)`
* A **Socket** is an **endpoint of a Connection**
* A **Port** is the *location* to which **packets** are shipped
* A **TCP Connection** is *defined by* **2 sockets**
* In general, a *Socket* needs to define its **transport protocol** (e.g. **TCP**)
* Every connection between a client and server requires a unique socket.


[S vs P]: http://stackoverflow.com/questions/152457

### What it is, what it do
**5/30/14**

* A **socket** is how a **process** (an 'object' managed by the OS), interacts with the network
* This is on the **application** layer (#7)
* It can shove a message into it, or receive a message from it

#### IP address, port, process

* You can send a message to a server by sending to it's **IP adress**,
* but you still need to make sure your message gets to the correct **process** on that server,
* which is why you append a **port number**.
* The server uses this to figure out which process should receive your message.
* For example, if you send it to **port 80**, the server will route it to a process that handles **HTTP requests**.

## SSL

### What it is, why it be
**5/30/14**

* TCP & UDP do not provide encryption
* **SSL provides an encrypted TCP connection**
	* **data integrity**
	* **endpoint authentication**
* SSL has a socket API

## Network Core

### Difference between a router and a switch
**5/29/14**

* From [about.com](http://compnetworking.about.com/od/homenetworkhardware/f/routervsswitch.htm) (not a great reference though)
* Traditionally, **routers** join together multiple **LAN**s with a **WAN**.
* They are intermediate destinations for network traffic, forwarding packets along toward their destinations.
* For example, your router at home connects your house's LAN to the Internet WAN.
* **Switches** (and **hubs**) can't do that.
* Instead, they just forward packets *within one LAN*.
* Switches (not hubs) can inspect the messages as they pass through, and forward data only to the specific device intended.

## REST

### Summary

**REST is an architectural style for developing distributed, networked systems and software applications such as the World Wide Web and web applications.** REST means that most *application components* (such as users and microposts) are modeled as *resources* that can be *created, read, updated, and deleted* -— operations that correspond both to the `CRUD` operations of *relational databases* and four fundamental `HTTP` request methods: `POST`, `GET`, `PATCH`, and `DELETE`.

##### 3/30/14

[Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content, expires 8/10/14](http://tools.ietf.org/pdf/draft-ietf-httpbis-p2-semantics-26.pdf)

HTML forms (up to and including HTML 5) can only send `GET` and `POST`.

### POST

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


#### The format

	POST /index.html HTTP/1.1
	Host: www.example.com
	Content-Type: application/x-www-form-urlencoded
	Content-Length: length
	
	licenseID=string&content=string&paramsXML=string

##### Key-Value pair encoding

For example, the key-value pairs

	Name: Jonathan Doe
	Age: 23
	Formula: a + b == 13%!

are encoded as

	Name=Jonathan+Doe&Age=23&Formula=a+%2B+b+%3D%3D+13%25%21

### PUT

Used to **create a resource, or overwrite it**. While you **specify the resources new URL**.

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

### PUT vs. POST

Here is the same thing stated over and over again in different ways, from [StackOverflow][PUTvPOST]:

* `PUT` is merely a statement of what content the service should, from now on, use to render representations of the resource identified by the client; `POST` is a statement of what content the service should, from now on, contain (possibly duplicated) but it's up to the server how to identify that content.
* `POST` means "create new" as in "Here is the input for creating a user, create it for me".
* `PUT` means "insert, replace if already exists" as in "Here is the data for user 5".
* `POST` to a URL creates a child resource at a server defined URL.
* `PUT` to a URL creates/replaces the resource in it's entirety at the client defined URL.
* `PATCH` to a URL updates part of the resource at that client defined URL.

### PUT vs. PATCH

The HTTP RFC specifies that **`PUT` must take a full new resource** representation as the request entity. This means that if for example only certain attributes are provided, those should be removed (i.e. set to null).

The semantics of **PATCH** are like PUT in that it updates a resource, but unlike PUT, it **applies a delta** rather than replacing the entire resource.
For simple resource representations, the difference is often not important, and **many APIs simply implement PUT as a synonym for PATCH**. [Restful API Design][]

[Restful API Design]: http://restful-api-design.readthedocs.org/en/latest/methods.html
[PUTvPOST]: http://stackoverflow.com/questions/630453/put-vs-post-in-rest/

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

