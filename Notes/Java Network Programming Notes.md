latex input:        mmd-article-header
Title:              Java Network Programming Notes
Author:         Ethan C. Petuchowski
Base Header Level:      1
latex mode:     memoir
Keywords:           Java, programming language, syntax, fundamentals
CSS:                http://fletcherpenney.net/css/document.css
xhtml header:       <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2014 Ethan Petuchowski
latex input:        mmd-natbib-plain
latex input:        mmd-article-begin-doc
latex footer:       mmd-memoir-footer

## Overview
* The *only* transport-layer protocols Java supports are TCP & UDP; for
  anything else, you must link to native code (JNI)
* Don't write to the network through a `PrintStream`
    * It chooses end-of-line chars based on your platform, not the protocol
      (HTTP uses `\r\n`)
    * It uses the default char encoding of your platform, not whatever the
      server expects
    * It eats all exceptions into this `boolean checkError()` method, when
      you're better off just using the normal exception hubbub

## class InetAddress

* `java.net.InetAddress` --- Java's representation of an IP address (v4 or v6)
    * DNS lookups are provided by this class
* Acquire one via a static factory

        InetAddress address = InetAddress.getByName("www.urls4all.com");

    This will look in your cache, and if it's not there connect to your DNS to
    get the IP address

## class URL

* Simplest way to locate and retrieve data from the network
* `final class java.net.URL extends Object` uses the *strategy design pattern*
  instead of inheritance to configure instances for different kinds of URLs
    * E.g. protocol handlers are strategies
* Think about it has having fields like
    * Protocol, hostname, port, path, query string, fragment identifier
* Immutable (makes it thread safe)
* Some constructors (all `throw MalformedURLException`)

        URL(String url)
        URL(String protocol, String hostame, String file)
        URL(String protocol, String host, int port, String file)
        URL(URL base, String relative)
* To get data from it you have

        InputStream   openStream()              // most common
        URLConnection openConnection([Proxy])   // more configurable
        Object        getContent([Class[]])     // don't use this
* Encode Strings into URLs using

        String encoded = URLEncoder.encode("MyCrazy@*&^ STring", "UTF-8");
    * There is a similar `decode(String s, String encoding)` method


## Jsoup

This is a 3rd party library for downloading and traversing Web content which
looks quite friendly.

## Snippet to search int array for available port

[From Stackoverflow][find port]

[find port]: http://stackoverflow.com/questions/2675362/how-to-find-an-available-port

    public ServerSocket create(int[] ports) throws IOException {
        for (int port : ports) {
            try {
                return new ServerSocket(port);
            }
            catch (IOException ex) {
                continue; /* try next port */
            }
        }
        throw new IOException("no free port found");
    }

Use it like so:

    try {
        ServerSocket s = create(new int[] { 3843, 4584, 4843 });
        System.out.println("listening on port: " + s.getLocalPort());
    }
    catch (IOException ex) { System.err.println("no available ports"); }

## Netty

* It is for implementing network protocols for e.g. when a plain-jane HTTP
  server is not going to cut it for serving your huge files.
* It is an asynchronous event-driven network application framework along with
  tooling for the rapid development of maintainable high-performance, high-
  scalability protocol servers and clients.
* It is a NIO client server framework which enables quick and easy development
  of network applications such as protocol servers and clients. It greatly
  simplifies and streamlines network programming such as TCP and UDP socket
  server development.

#### Sources
* [Netty User Guide](http://netty.io/wiki/user-guide-for-4.x.html)
