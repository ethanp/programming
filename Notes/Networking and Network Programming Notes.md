## [SOAP](http://en.wikipedia.org/wiki/SOAP)

### TODO

### Simple Object Access Protocol

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

