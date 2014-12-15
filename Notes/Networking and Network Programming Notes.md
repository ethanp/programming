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
    * **POP3** (Post Office Protocol Version 3) -- for retrieving email from
      mail server
    * **IMAP** (Internet Message Access Protocol) -- for retrieving email from
      mail server
    * **DNS** (Domain Name System) -- for translating name to an IP address
    * **ICMP** (Internet Control Message Protocol) -- provides error messages

### Networking Concepts

* **Host** -- provides an endpoint for networked communication
* **Packet** -- small piece of the data to be sent (see IP, below)
* **Latency** -- request's *round-trip time*
* **Firewall** -- a *router* that inspects, modifies, or blocks specific traffic
  flowing through it.


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
* Packets can get lost, reordered, duplicated (, corrupted? not sure)

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
