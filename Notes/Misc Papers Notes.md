latex input:    mmd-article-header
Title:          Misc Papers Notes
Author:         Ethan C. Petuchowski
Base Header Level:      1
latex mode:     memoir
Keywords:       algorithms, computer science, theory, grammars
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
copyright:      2015 Ethan Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer

# Distributed Computing

## The Akamai Network: A Platform for High-Performance Internet Applications

> Nygren, Erik, Ramesh K. Sitaraman, and Jennifer Sun. "The akamai network: a
> platform for high-performance internet applications." ACM SIGOPS Operating
> Systems Review 44.3 (2010): 2-19.

### Introduction

1. The Internet is too slow to conduct global business
2. Akamai invented Content Delivery Networks in the 1990s
3. 2010, Akamai delivers 15-20% of global Web traffic, as well as DNS,
   analytics, live streaming HD video, etc.
4. They believe a *highly distributed network* is the most effective
   architecture for these purposes

### Internet Application Requirements

1. End-to-end system quality --- *any* outages are *very* costly
2. Sheer website performance is important for maintaining customer loyalty
3. Increasing website performance makes customers more likely to purchase
4. The Internet itself provides no end-to-end reliability or performance
   guarantees

### Internet Delivery Challenges

1. The Internet is composed of 1000s of networks
2. The largest has only 5% of Internet access traffic
3. It takes 650 networks to get to 90% of all access traffic
4. So centrally-hosted content must travel through multiple networks to reach
   end-users
5. No money is made on the "middle-mile" *peering points* where one network
   connects to a competitor's, thus creating bottlenecks/congestion causing
   packet loss and increased latency
6. **BGP (Border Gateway Protocol)** --- used for exchanging routing
   information between ISPs. Layer 4, sits atop TCP.
7. BGP is inefficient and subject to business agreements, misconfiguration,
   foul play, hijacking, etc. and can cause route flapping, bloated paths, and
   broad connectivity outages
8. Internet outages and partitions happen all the time
9. TCP carries significant overhead, and is a bottleneck for video because
   every window of packets must be ACKd, which means throughput is inversely
   related to round trip time (RTT/network latency)
10. Origin server capacity needs to scale quickly with demand
11. People's continued use of IE6 and other old technology means algorithm
    improvements can't break their ability to access content

### Delivery Network Overview

1. CDNs originally just cached static site content at the "edge" of the
   Internet, close to end users to avoid middle-mile bottlenecks
2. Now they accelerate entire web apps and provide HD live streaming media
3. They provide security, logging, diagnostics, reporting, and management tools

#### Delivery Networks as Virtual Networks

1. **Delivery network** --- *virtual network* built as a software layer over
   the actual Internet
2. Provides reliability, performance, scalability, and security
3. Works over the existing Internet as-is, requiring no client software and no
   changes to the underlying networks

#### Anatomy of a Delivery Network

1. Akamai's network has 10k's of globally deployed servers running
   sophisticated algorithms to enable faster content delivery
2. Different servers are running different algorithms for optimize for
   different things (e.g. dynamic content vs streaming media)
3. User enters URL into browser, URL is translated by the **mapping
   system** into the IP address of an **edge server** using historical and
   current data with machine learning
4. The *edge server* sends the requested data to the user
5. If the *edge server* needs to request data from the **origin server**,
   it uses the highly reliable and performant **transport system**
6. *Communications and control system* --- disseminates control messages
   and configuration updates
7. *Data collection and analysis system* --- monitoring, analytics, billing
8. *Management portal* --- configuration management platform for customers
   with analytics about customer usage and demographics

#### System Design Principles

1. Delivery networks were designed with the philosophy that failures are normal
   and everything must operate seamlessly despite them, so there are no single
   points of failure
2. Also, highly automated, scalable, performant

### High-Performance Streaming and Content Delivery Networks

1. Instead of dozens of massive datacenters of servers, there are 1000s of
   locations with server clusters

#### Video-grade Scalability

1. In the next 2--5 years, throughput requirements will grow by an order of
   magnitude
2. The bottleneck will no longer be just the origin data center, but it could
   be the peering point or network backhaul capacity
3. Thus CDNs will be even *more* of a necessity
4. Software multicast implementations have been far more practical than IP-
   layer multicast

#### Streaming Performance

1. Akamai optimizes stream availability, startup time, frequency and duration
   of playback interruptions, and effective bandwidth
2. They built a global monitoring infrastructure that simulates users to test
   quality and collect data

#### A transport System for Content and Streaming Media Delivery

1. Recall from above, the **transport system** connects the *edge servers* with
   the *origin servers*
2. Edge servers use excellent caching strategies
3. *Tiered distribution* involves a cache pyramid with "parent" clusters
   holding more content
4. When new content is made available for streaming, it is sent to multiple
   *entrypoints* and then there's an intermediate layer of *reflectors* which
   relay streams (according to their own software routing algorithms) to *edge
   clusters*

### High-Performance Application Delivery Networks

#### A Transport System for Application Acceleration

1. Akamai has highly optimized inter-edge-server communication which reduces
   packet loss
    1. path optimization --- instead of BGP's chosen path
    2. protocol enhancements (because they're not constrained by client
       software adoption rates) --- including persistent connection pools,
       dynamic TCP window sizing, overriding standard TCP timeout and
       retransmission protocols
2. Application-wise, **edge-servers can prefetch content** before the user's
   browser requests it, **so dynamic content *appears* to be cached**.
   Customers can decide the specifics of how this is done
3. Content compression is useful for end users with slow connections
4. Having a great overlay network makes for good long-distance performance---
   for large files, for example, **origin server downloads that go over the
   high performance overlay can perform nearly as well as files delivered from
   cache because the overlay is able to deliver the file from origin to edge
   server as quickly as the edge server can deliver to the end user**.

#### Distributing Applications to the Edge

1. Akamai EdgeComputing takes cloud computing to a new level of performance,
   reliability, and scalability by distributing the application *itself* to the
   "edge", so application resources are allocated not only on-demand but also
   near the end user.
2. Applications relying heavily on large transactional databases still require
   significant communication with the origin server
3. However there are use cases, like content aggregation and reformatting,
   static databases (product catalogs), data validation and data input
   batching, and static pieces of dynamic content

### Platform Components

#### Edge Server Platform
1. Customers can tune exactly how the edge servers are to be used

#### Mapping System

1. The scoring system first creates a current topological map capturing the
   state of connectivity across the entire Internet. This requires collecting
   and processing tremendous amounts of historic and real-time data—including
   pings, traceroutes, BGP data, logs, and IP data, collected cumulatively over
   the years and refreshed on a continual basis.
2. This is used by the Akamai platform to direct end users to the best edge
   servers and to select intermediates for tiered distribution and the overlay
   network.
3. This system is all highly distributed and fault-tolerant

#### Communications and Control System

1. For small status and control messages, they use public/subscribe with multi-
   path tiered fan-out
2. Configuration updates are published to storage servers with quorum-based
   replication

#### Data Collection and Analysis System

1. Collect 100TB of logs per day, compressed and aggregated to a set of clusters with dedicated processing pipelines, then passed to systems for analytics, storage, and delivery to customers
2. Real-time monitoring of status information of just about every software
   component can be done through a standard SQL interface to their Query system

#### Additional Systems and Services

1. DNS, network and website performance monitoring agents, medium-term storage,
   management portal with analytics, etc.

### Example: Multi-Level Failover

1. DNS resolution first goes to generic Top Level Domain servers, then Akamai's
   Top Level Name Servers, then Akamai Low Level Name Servers, which returns an
   edge server IP address based on the mapping system from above. Then the
   browser makes an HTTP request to the edge server.
2. If a machine fails, another machine will start responding to that IP address
   and the low level map is updated
3. If a cluster fails it will be removed from the map
4. If a connection degrades, the path optimization will no longer use it

### Customer Benefits and Results

#### Customer Examples for Content and Streaming Delivery

1. Improved performance, reliability, infrastructure cost savings, protection
   from DDoS, ability to handle flash crowds

#### Customer Examples for Application Delivery

1. Performance, reliability, less latency for international traffic, large file transfers using Akamai's overlay network, avoid building out regional data centers
