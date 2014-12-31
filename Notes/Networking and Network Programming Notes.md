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
  that 7-layer OSI Model
    * Physical, Data Link, Network, Transport, Session, Presentation,
      Application
* They generally follow a 4-layer abbreviation
    * Link \\(\rightarrow\\) Internet \\(\rightarrow\\) Transport
      \\(\rightarrow\\) Application
* "The Internet" is the world's largest IP-based network
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
        * **SOAP**
        * **XML-RPC**
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

### Sockets & Ports

* Ports have a number between 1 and 65,535 (2 bytes)
* Ports up to 1023 are reserved for *well-known services*
* **TCP Socket** --- endpoint of a Connection
    * `(IP-Address) + (Port Number)`
    * An application can shove a message into it, or receive a message from it
* **TCP Port** --- *location* to which packets are shipped
* **TCP Connection** --- 2 sockets (one on each end)
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

### SOAP (Simple Object Access Protocol) (TODO)

Uses XML, kind of old-school now, but I guess enterprises use it?

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
