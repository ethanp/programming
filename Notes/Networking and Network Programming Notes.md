latex input:    mmd-article-header
Title:          Networking and Network Programming Notes
Author:			Ethan C. Petuchowski
Base Header Level:		1
latex mode:     memoir
Keywords:       REST protocol, protocols, sockets, TCP/IP
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2014 Ethan C. Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

## Internets have Layers, like an Onion

* **Internet implementations** (TCP over IP) don't actually strictly follow
  that **7-layer OSI Model**
    * Physical, Data Link, Network, Transport, Session, Presentation,
      Application
* They generally follow a **4-layer abbreviation**
    * **Link \\(\rightarrow\\) Internet \\(\rightarrow\\) Transport
              \\(\rightarrow\\) Application**
* **"The Internet"** --- the world's largest IP-based network
    * No one owns or controls it, it's just a bunch of nodes that know how to
      communicate with each other.

#### TCP/IP Layers

1. **Network Interface / Link** -- (OSI 1,2) -- uses the hardware to do the
   above layers
    * **Ethernet**
    * **Wifi**
2. **Internet** -- (OSI 3) -- congestion control and routing global unique IP
   addresses and IP packets
    * **IP** (Internet Protocol) -- addresses hosts and routes packets across
      1+ IP networks
3. **Transport** -- (OSI 4) -- error control, segmentation, congestion
   control, application addresssing
    * **TCP** (Transmission Control Protocol) -- creates *reliable* connection
      between 2 computers
    * **UDP** (User Datagram Protocol) -- *broadcasts* messages over a network
4. **Application** -- (OSI 5,6,7) -- protocols used by applications
    * **FTP** -- for transmitting files, out-of-date (see below)
    * **SMTP** (Simple Mail Transfer Protocol) -- for sending mail between
      mail servers
    * **HTTP** -- used by the WWW
        * **SOAP** [or old-school XML-RPC]
        * **REST API**
    * **POP3** (Post Office Protocol Version 3) -- for retrieving email from
      mail server
    * **IMAP** (Internet Message Access Protocol) -- for retrieving email from
      mail server
    * **DNS** (Domain Name System) -- for translating name to an IP address
      (uses UDP)
    * **ICMP** (Internet Control Message Protocol) -- provides error messages
        * *Traceroute*
        * *Ping*

### Networking Concepts

* **Host** -- provides an endpoint for networked communication
* **Packet** -- small piece of the data to be sent (see IP, below)
    * Allows for wire-time to shared effectively, and for integrity checks
* **Latency** -- request's *round-trip time*
* **Firewall** -- software in the *router* (and your OS may have one) that
  inspects, modifies, or blocks specified traffic flowing through it
* **Node** -- a machine on a network
* **Address** -- a sequence of bytes uniquely identifying a *node*
    * In IPv4 these are 4 full unsigned bytes (0-255), e.g. 199.1.32.90
        * This allows for 4 billion *total*, not enough to have one per
          person...
        * Asia, Australia, and Europe ran out by 2013
    * In IPv6 these are 16 bytes, e.g.
      `FEDC:BA98:7654:3210:FEDC:BA98:7654:3210`
* **Protocol** -- a precise set of rules defining how computers communicate
* **DNS** (Domain Name Service) -- translates *hostnames* into IP addresses
* `127.0.0.1` is the *local loopback address* -- always points to the local
  computer (*hostname* `localhost`)
    * For IPv6 it's `0:0:...:0:1` aka `::1`
* `255.255.255.255` is *broadcast* to everyone on the local network, and is
  used to find the local DHCP server
* **NAT** (Network Address Translation) -- your internal IP address within
  your LAN is different from your external IP address to The Internet, and
  this translation is done/managed by your router
    * It could be that your router is only using up a single IP address for
      all devices in your house
* **Proxy server** -- you connect to the outside world through this server
    * It has a different IP address, so that the outside world never learns
      about your real IP address
    * It can do more thorough inspection of packets being sent by and to you
    * It can be used to implement local area caching
* **Peer-to-peer** -- an alternative to the *client/server* model, where any
  node can initiate a connection with any other

## Transport Layer

### TCP (Transmission Control Protocol)
**9/5/14**

* This is the **transport layer** of the **TCP/IP suite**
* Intermediary between the application and Internet Protocol (IP)
    * An app simply issues a *single* TCP request with the data it wants to
      transmit, and TCP handles the packet breakup and IP requests etc.
* IP packets can be lost, duplicated, or delivered out of order
    * TCP handles all this; specifically, it *guarantees* that all bytes are
      perfectly received in the correct order
* TCP uses *positive acknowledgement with retransmission* as the basis of its
  algorithm
    * Sender keeps a record of each packet it sends
    * Sender maintains a timer from when each packet was sent
        * Sender retransmits if no ACK is received before *timeout* (due to
          loss or data corruption)
    * Receiver responds with an ACK message as it receives the packet
    * The actual algorithm is not in these notes at this time.
* **TCP Connection** --- 2 *sockets* (one on each end) (*sockets defined
  below*)

### Sockets

* **Network Socket** --- endpt of interprocess communication across a network
* **Internet Socket** --- unique combination of

        localSocketAddress + remoteSocketAddress + protocol
* **Socket Address** --- `ipAddress + port`
* **Socket descriptor** --- `int` that references the socket within the OS
    * The OS uses this to strip the routing info and forward the data to the
      stream-reader object within the application
* The normal **socket API** is based on the **Berkeley sockets** standard
    * This is the origin of such actions as
        * `socket()` --- construct socket and allocate OS resources to it
          (e.g. slot in descriptor table)
        * `bind()` (server side) --- associate socket with IP addr + port
        * `listen()` (server side) --- TCP socket enters "listening" state
        * `connect()` (client side) --- associate socket to local port number;
          and for TCP, attempt to establish TCP connection
        * `accept()` (server side) --- accept attempt to create a new TCP
          connection, and create a new socket for it
        * `send() recv() write() read()` etc. --- self-explanatory
        * `close()` --- release resources allocated to socket; for TCP close
          connection
        * `gethostbyname() gethostbyaddr()` --- resolve host names and
          addresses
        * `select()` --- wait for one or more provided sockets to be ready to
          read or write or retrieve errors from
        * `poll()` --- test if socket can be read from, written to, or if
          there are errors to retrieve
        * `get`/`setsockopt()` --- get/set socket options

#### Ports

* Ports have a number between 1 and 65,535 (2 bytes)
* Ports up to 1023 are reserved for *well-known services*
* Each connection between a client and server requires its own unique socket.
* You can send a message to a server by sending to it's **IP adress**,
* You append a **port number** to make sure your message gets "demultiplexed"
  to the correct **process** on that server,

## Network Layer
### IP (Internet Protocol)

* Exchanges **packets** --- which have a **header** and a **body**
    * **Header** -- source, destination, control info
    * **Payload** -- the *data*
    * **Trailer** -- *checksum* (sometimes inside the header)
        * Only for detecting corruption in header itself, *not* the data
* Packets can get lost, reordered, duplicated, or corrupted

## HTTP

### Status codes

* `100s` --- informational response
* `200s` --- request succeeded
* `300s` --- redirection
* `400s` --- client error
* `500s` --- server error

### Methods

#### Main ones
* `GET` --- retrieve, idempotent, side-effect free
* `POST` --- upload reseource without specifying a any action, not idempotent
* `PUT` --- idempotent, upload representation of resource to server
* `DELETE` --- remove resource from specified URL, idempotent

#### Other ones
* `HEAD` --- only download resource header
    * e.g. to check `mtime` to see if cache is valid
* `OPTIONS` --- ask server what can be done with specified resource
* `TRACE` --- echo back client request

#### Non-standard ones
* `COPY`
* `MOVE`

### Request

This is from *Harold, Elliotte Rusty (2013-10-04). Java Network
Programming. O'Reilly Media. Kindle Edition.* It may contain typos.


    GET /index.html HTTP/1.1
    User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv: 20.0)
    Host: en.wikipedia.org
    Connection: keep-alive
    Accept-Language: en-US,en;q=0.5
    Accept-Encoding: gzip, deflate
    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8

### Response

This is from Wikipedia's page on HTTP.

    HTTP/1.1 200 OK
    Date: Mon, 23 May 2005 22:38:34 GMT
    Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)
    Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT
    ETag: "3f80f-1b6-3e1cb03b"
    Content-Type: text/html; charset=UTF-8
    Content-Length: 131
    Accept-Ranges: bytes
    Connection: close

    <html>
    <head>
      <title>An Example Page</title>
    </head>
    <body>
      Hello World, this is a very simple HTML document.
    </body>
    </html>

## REST

### Summary

* An architectural style for developing distributed, networked systems such as
  the World Wide Web and its applications. *Application components* (e.g.
  `users` and `tweets`) are *resources* that can be *created, read, updated,
  and deleted* [CRUD], corresponding to the four fundamental `HTTP` request
  methods: `POST`, `GET`, `PATCH`, and `DELETE`.
* HTML forms (up to and including HTML 5) can only send `GET` and `POST`.

### POST

* **Providing a block of data** to a handling process
    * Fields from an HTML form
    * A message for a bulletin board
    * A new resource
    * Appending data to a resource

### If your action is not *idempotent*, then you *MUST* use `POST`

**`GET`, `PUT` and `DELETE` methods are required to be idempotent.** The client
should be able to pre-fetch every possible `GET` request for your service
without it causing visible side-effects.

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

Create a resource, or overwrite it at the specified new URL.

A successful `PUT` of a given representation would suggest that a subsequent
`GET` on that same target resource will result in an equivalent representation
being sent in a `200 (OK)` response. `PUT` is **idempotent**, so duplicate
attempts after a successful one have no effect.

### PUT vs. POST

* `POST` means "create new" as in "Here is the input for creating a user,
  create it for me".
* `PUT` means "insert, replace if already exists" as in "Here is the data for
  user 5".
* `PATCH` to a URL updates part of the resource at that client defined URL.

### PUT vs. PATCH

`PUT` must take a full new resource representation as the request entity.
`PATCH` also updates a resource, but unlike PUT, it *applies a delta* rather
than replacing the entire resource. Many APIs simply implement PUT as a
synonym for PATCH.

## Some More Protocols

### SSL (Secure Sockets Layer) / TLS (Transport Layer Security)

* TCP & UDP do not provide encryption on their own
* **SSL provides an encrypted TCP connection**, yielding improved
    * data integrity
    * endpoint authentication
* In addition to encrypting data over the wire (like SSL), TLS authenticates a
  server with a certificate to prevent spoofing.
*  Uses long-term public and secret keys to exchange a short term session key
   to encrypt the data flow between client and server

#### HTTPS (HTTP Secure)

* Provides **authentication** of the website and associated web server that one
  is communicating with, which protects against *man-in-the-middle* attacks
* Also provides **bidirectional encryption** of communications between a client
  and server, which protects against *eavesdropping* and *tampering* with or
  *forging* the communication's contents
* Everything in the HTTPS message is encrypted, including the headers, and the
  request/response load.
* Technically, not a protocol in and of itself, but the result of layering
  `HTTP` on `SSL`/`TLS`
* Relies on *certificate authorities* to verify the owner of the certificate
    * Snowden's documents revealed that this *still* allows *man-in-the-middle*
      attacks
* Note: a site *must* be *completely* hosted over HTTPS (without having some of
  its contents loaded over HTTP) or the user will be vulnerable to some attacks
  and surveillance.
* Uses `port 443` by default (not `80`)
* To serve over HTTPS without the client's browser showing a warning, one must
  create a public key certificate signed by a certificate authority. This may cost $8 -- $70 per year.

### SOAP (Simple Object Access Protocol)

* A *protocol specification* for exchanging *structured information* in the
  implementation of a *Web service*
* Uses XML for its message format,
* Relies on e.g. HTTP or SMTP for message transmission
* Can form the foundation layer of a web services protocol stack
* Has 3 parts
    1. An envelope, i.e. **message structure** and how to process it
    2. Encoding rules for application-defined **datatypes**
    3. A convention for representing *procedure calls* and *responses*
* Has 3 characteristics
    1. Extensible (not sure what this means)
    2. Neutral (doesn't care what transport protocol is used)
    3. Independent (of programming language etc.)
* Evolved as successor of `XML-RPC`
* Designed in 1998 for Microsoft, became W3C recommendation (with
  specification) in 2003

#### Wikepedia's example message

    POST /InStock HTTP/1.1
    Host: www.example.org
    Content-Type: application/soap+xml; charset=utf-8
    Content-Length: 299
    SOAPAction: "http://www.w3.org/2003/05/soap-envelope"
     
    <?xml version="1.0"?>
    <soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
      <soap:Header>
      </soap:Header>
      <soap:Body>
        <m:GetStockPrice xmlns:m="http://www.example.org/stock">
          <m:StockName>IBM</m:StockName>
        </m:GetStockPrice>
      </soap:Body>
    </soap:Envelope>

### XMPP (Extensible Messaging and Presence Protocol aka "Jabber") -- 1999

* Message-oriented middleware based on XML
* Open standard, many free and open source software implementations
* Instant messaging (IM) (including multi-user), VoIP, video, file transfer,
  gaming, Internet of Things, smart grid, social networking
* Transport over TCP, or HTTP, or WebSocket; optionally uses TLS
* No central master server, but it's neither anonymous nor peer-to-peer
* Client-server architecture, clients don't talk directly to each other

### Telnet

From 1968, an unencrypted-but-otherwise-SSH-like protocol

### SIP (Session Initiation Protocol)

* A text-based *session (application) layer* *signaling* *communications
  protocol* from 1996 for controlling e.g. Voice/Video Over IP for two-party
  ("unicast") or multiparty ("multicast") *sessions*, as well as file transfer
  and online games; runs over over TCP, UDP, or SCTP
    * Defines messages for establishment, termination, and other essential
      elements of a call liike changing addresses or ports, inviting more
      participants, and adding or deleting media streams
    * The media itself is transmitted over another application protocol,
      RTP/RTP (Real-time Transport Protocol) *(see below)*
    * Like HTTP, each transaction consists of a client request invoking a
      particular method/function on the server, and at least one response;
      most devices can perform both the client and server roles, where caller
      is recipient and callee is server
    * Request methods include REGISTER, `INVITE`, `ACK`, `CANCEL`, `BYE`, and
      `OPTIONS`
    * Response codees include Provisional (`1xx`), Success (`2xx`),
      Redirection (`3xx`), Client Error (`4xx`), Server Error (`5xx`), and
      Global Failure (`6xx`)
    * Reuses most of the header fields, encoding rules, and status codes of
      HTTP
    * Each resource in a SIP network as a URI with the format
      `sip:username:password@host:port`, or `sips:` for secure transmission
      via TLS
* **Signaling** --- message to inform receiver of a message to be sent
    * E.g. to establish a telecommunication circuit
* **Communications Protocol** --- system of digital rules for data exchange
  within or between computers.
    * Well-defined message formats with exact meaning provided by a specified
      syntax, semantics, and synchronization of communication
* **Communication session** --- "semi-permanent interactive information
  interchange between 2+ devices" (lol). *Established* and *terminated* at
  specific points in time.

### RTP (Real-time Transfer Protocol)

* Defines a standardized packet format for delivering audio and video over IP
* Often uses RTCP (RTP Control Protocol) for monitoring quality of service
  (QoS) and SIP (Session Initiation Protocol) *(see above)* for setting up
  connections across the network
* Designed for end-to-end, *real-time* transfer of *stream* data, provides
  *jitter* compensation and detection of out of sequence data arrival. Allows
  use of IP *multicast*.
    * **Jitter** --- deviation from true periodicity of a presumed periodic
      signal (e.g. variation of packet latency). Dejitterizers use a buffer.
    * **Real-time** programs *must guarantee* response within strict time
      constraints aka "deadlinees"
    * **Streaming media** --- bresented to end-user *while* being delivered by
      provider
* Tolerates some packet loss to achieve goal of real-time multimedia streaming
* Generally uses UDP and not TCP

## Miscellaneous

### Overlay Network

* A network built on top another network
* A link between 2 nodes in the overlay may require multiple links in the underlying network
* The Internet was originally an overlay upon the telephone network.  Today,
  VoIP is the vice-versa.
* Distributed systems such as peer-to-peer networks and client-server
  applications are overlays over the Internet

### Router vs. Switch

* **Routers** join together multiple **LAN**s with a **WAN**
* Intermediate destinations, forwarding packets toward their destinations
* Your home router connects your house's LAN to the Internet WAN
* **Switches** just forward packets *within one LAN*
    * They can inspect the messages, and forward data only to the intended
      device

### Apple's general Network Programming Tips

* Higher-level APIs solve many networking problems for you -- caching,
  proxies, choosing from among multiple IP addresses for a host, and so on
* Be wary of all incoming data. Carefully inspect incoming data and
  immediately discard anything that looks suspicious
* Bad networking code can cause poor battery life, performance, and so on
* Batch your transfers, and idle whenever possible
    * If you keep their radio on, you're wasting their battery life
* Cache resources locally, only redownload if you know it changed

# Distributed Systems

Some notes from *"From P2P and Grids to Services on the Web: Evolving
Distributed Communities"*, Taylor & Harrison, 2nd Ed. Springer: Computer
Communications and Networks, 2009.

## What is a "Service"

> A service is a software entity that can be used to represent *resources*, and
therefore capabilities, on a network. Services receive a reqest, and
(optionally) return a response; similar to a function call. --- Taylor &
Harrison, pg. 8

### Web service

Some from [Wikipedia: *Web service*][wws] and other Wikipedia pages

1. **a method of communication between two electronic devices**
over a network.
2. It is **a software function provided at a network address** over the Web
   with the service always on as in the concept of *utility computing*.
    * **Utility computing** --- the packaging of computing resources, such as
      computation, storage and services, as a metered service
3. The W3C defines a Web service generally as: *a software system designed to
   support **interoperable** machine-to-machine interaction over a network.*
    * **Interoperability**
        * **Syntactic interoperability** --- when two or more systems are
          capable of communicating and *exchanging data*. Relies on specified
          data formats & communication protocols such as XML or SQL.
        * **Semantic interoperability** --- the ability to automatically
          *interpret* the information exchanged meaningfully and accurately.
          The content of the information exchange requests are unambiguously
          defined.
4. **REST-compliant Web services** --- manipulate XML representations of Web
   resources using a uniform set of stateless operations
5. **Web Services Description Language** (**WSDL**: "wiz'-dul") --- an XML-
   based interface definition language that is used for describing the
   functionality offered by a web service.
    * Filename extension `.wsdl`
    * Provides a machine-readable description of how the service can be called,
      what parameters it expects, and what data structures it returns,
      basically like method signatures.

[wws]: http://en.wikipedia.org/wiki/Web_service

### Service Oriented Architecture (SOA)

* A collection of loosely-coupled services on a network that communicate with
  each other over well-defined interfaces.
* Capabilities are dynamically discoverable
* It is possible to quickly assemble impromptu computing communities without
  human intervention
* An SOA does *not* require using Web Services and obviously vice-versa too

## Distributed Objects

* E.g. in CORBA (Java, Python, C++, etc.), you instantiate an object which gets
  serialized with its entire class hierarchy and you receive a handle to it (a
  URI). You don't know where it actually exists on the network.
* *Mobile agents* are little bundles of code that can travel to different hosts
  and execute or even replicate themselves. E.g. a MapReduce program, in which
  the algorithm travels *to* the data and returns the results to the invoker.

## Grid Computing

* The term is from an analogy with the electrical power grid: users can tap
  into resources off the internet like plugging into a power outlet
* At this time there is no one Grid, but many different types that are possibly
  evolving, private, public, regional, global, specific in goal, generic in
  goals, etc.
* Condor is one example: you send the code you want to run to a resource
  manager, who dynamically locates available processing and storage resources
  and submits jobs to them and retrieves results which are returned to you

## P2P Application Architectures

### Gnutella

1. No single entity can be isolated to bring down the entire network
2. The information providers are not indexed in a central place
3. **Servent** (peer) = **serv**er + cli**ent**
4. Each servent only sees the (roughly 4) other servents it is directly
   connected to
5. **To search for a file**, a peer asks its neighbors, which is forwarded to
   further neighbors until something is found
    1. Done naively, this will congest the network without leaving room for
       file transfer
6. Requests that have already been seen are because of loops in the semi-random
   overlay topology and are dropped (by remembering request IDs)
7. TTL ("Gnutella horizon") for requests is 7 hops (up to 10,000 reachable
   nodes)
8. **To join the network and discover peers**
    1. **Out-of-band methods** --- ask on IRC, check a handful of Web pages,
       try ones that have worked before
    2. **GnuCache** --- a permaent server users can connect to to find peers
    3. **Internal Peer Discovery** --- once a single peer is known, you can
       send it a *ping* which it forwards to its neighbors, and so on until
       some peers send you a *pong* which means they can become your new direct
       neighbors

#### Gnutella Protocol

1. Comprises of a set of descriptors (packets) and rules of exchange

##### Descriptors for finding a file

1. Header --- descriptor ID (16-bytes), payload type, TTL, hops, payload
   length
2. Payload --- one of the following types
    1. Ping (announce) --- empty
    2. Pong (reply to Ping) --- port, IP Addr, # files shared, # KB shared
    3. Query (search the network) --- minimum speed, search criteria (a
       null-terminated string)
    4. QueryHit (reply to search) --- # of hits, port, IP Addr, speed,
       result set (variable length), servent ID
        1. Result set entry --- file index (file ID), file size, file name
    5. Push (traverse firewalls) --- servent ID, file index, IP Addr, port

##### Downloading a file

Requesting servent issues an `HTTP GET` request

    GET /get/<fileIndex>/<fileName> HTTP/1.0]\\r\\n
    Connection: Keep-Alive\\r\\n
    Range: bytes=0-\\r\\n
    User-Agent: Gnutella \\r\\n
    \\r\\n

Receiving servent replies `HTTP OK`

    HTTP 200 OK\\r\\n
    Server: Gnutella\\r\\n
    Content-type: application/binary\\r\\n
    Content-length: 567890\\r\\n
    \\r\\n

Then the file is sent.
