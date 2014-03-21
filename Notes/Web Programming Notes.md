Websockets
----------
#### 3/21/14

### [Wikipedia](http://en.wikipedia.org/wiki/Websockets)

* **WebSocket** -- a *protocol* providing *full-duplex* communications
  channels over a *single TCP connection*.
* **Full-duplex** -- data can go both directions across the connection at the same time
* First standardized in 2011
* Facilitates live content and real-time games
* Enables an ongoing back and forth between browser and server
* Happens over TCP port 80
* Server or client can easily close it.
* Huge performance and architecture advantages over AJAX or Long Poll
* Uses URI schems `ws:` and `wss:` for unencrypted and encrypted connections respectively
* Handshake looks like a HTTP `GET` request/response sequence, but it's actually
  an a protocol-upgrade request.

Client request:

    GET /chat HTTP/1.1
    Host: server.example.com
    Upgrade: websocket
    Connection: Upgrade
    Sec-WebSocket-Key: x3JJHMbDL1EzLkh9GBhXDw==
    Sec-WebSocket-Protocol: chat, superchat
    Sec-WebSocket-Version: 13
    Origin: http://example.com

Server response:

    HTTP/1.1 101 Switching Protocols
    Upgrade: websocket
    Connection: Upgrade
    Sec-WebSocket-Accept: HSmrc0sMlYUkAGmm5OPpG2HaGWk=
    Sec-WebSocket-Protocol: chat

### [WebSocket.org](https://www.websocket.org/quantum.html)

* "HTML5 Web Sockets" provides a dramatic improvement from the old, convoluted
  hacks that are used to simulate a full-duplex connection in a browser
* Older techniques for achieving this included *long-polling* and *streaming*,
  often wrapped into **Comet**-based "pushes"
* Simply put, HTTP wasn't designed for real-time, full-duplex communication,
  it is a half-duplex model
* WebSockets are in the HTML5 specification
* Once a WS connection is established by handshake (see above), both text and
  binary frames can be sent full-duplex, in either direction at the same time.
* The data is minimally framed with just two bytes.

### [LostTechies.com](http://lostechies.com/chrismissal/2013/08/06/browser-wars-websockets-vs-ajax/)

* Because the connection is persistent, the server can now initiate
  communication with the browser. The server can send alerts, updates,
  notifications.
* This adds a whole new dimension to the types of applications that can be
  constructed.
* WebSockets are a boon to the *Internet of Things* (good point...)
* "In point of fact, the entire AJAX protocol could be built using Websockets
  technology. This makes Websockets a literal superset of AJAX."


AJAX
----
#### 3/21/14

**Asynchronous JavaScript and XML**

[Wikipedia](http://en.wikipedia.org/wiki/Ajax_(programming))

* Has a specific field for sending arbitrary text, and another for XML
* Nowadays, people just use the text one for sending JSON
* Create connection to server on each request, sends request (with possible
  extra data), and gets response from server. Then connection closes.
* It is *single request > response for each AJAX call*.
* JavaScript and the `XMLHttpRequest` object provide a method for exchanging data
  asynchronously between browser and server to avoid full page reloads.
* Term first appeared in 2005

Downsides:

* *In pre-HTML5 browsers*, hitting the back buttun didn't have the intended effect
* *In pre-HTML5 browsers*, bookmarking didn't have the intended effect
* If user's connection is slow, it might do things at the wrong time
* Most web-crawlers don't execute JavaScript
* It makes it harder to make web-page accessible to screen-reader technologies
* It leads to trickier callback-ridden code that is harder to maintain, debug, and test

**Example** using jQuery:

    $.get('send-ajax-data.php', function(data) {
        alert(data);
    });


Port
----
#### 3/21/14

[Wikipedia](http://en.wikipedia.org/wiki/Port_(computer_networking))

* The purpose of ports is to uniquely identify different applications or
  processes running on a single computer and thereby enable them to share a
  single physical connection to a packet-switched network like the Internet.
* Data packets are routed across the network to a specific destination IP
  address, and then, upon reaching the destination computer, are further routed
  to the specific process bound to the destination port number.
* Used by *Transport Layer* protocols, such as *TCP* and *UDP*
* 16-bit *port number*
* Some example **Well-known ports**:
    * 22 -- SSH
    * 53 -- DNS
    * 80 -- HTTP
    * 194 -- IRC
    * 465 -- SMTP Secure

Socket
------

* IP Addres + Port
* A single bidirectional connection between two network applications (can even be on the same computer)

Socket.IO
---------
#### 3/21/14

* "Realtime application framework for Node.JS, with HTML5 WebSockets and
  cross-browser fallbacks support."
* "Aims to make realtime apps possible in every browser and mobile device,
  blurring the differences between the different transport mechanisms."

Comet
-----
#### 3/21/14

* Old-school way of doing full-duplex over HTTP, replaced by WebSockets
* A long-held HTTP request allows a web server to push data to a browser,
  without the browser explicitly requesting it.
* Comet is an umbrella term, encompassing multiple techniques for achieving
  this interaction.
* All these methods rely on features included by default in browsers, such as JavaScript
* **Long-polling** -- the browser sends a request to the server and the server
  keeps the request open for a set period. If a notification is received within
  that period, a response containing the message is sent to the client. If a
  notification is not received within the set time period, the server sends a
  response to terminate the open request.
* **Streaming** -- the browser sends a complete request, but the server sends
  and maintains an open response that is continuously updated and kept open
  indefinitely (or for a set period of time). The response is then updated
  whenever a message is ready to be sent, but the server never signals to
  complete the response, thus keeping the connection open to deliver future
  messages.

Backbone.js
-----------

Data models for JavaScript?

Angular.js
----------

RabbitMQ
--------

Scalable, asynchronous messenging

