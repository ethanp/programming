latex input:		mmd-article-header
Title:				Simple P2P
Author:			Ethan C. Petuchowski
Base Header Level:		1
latex mode:		memoir
Keywords:			Java, Networking, P2P Systems, Peer-to-peer, BitTorrent
CSS:				http://fletcherpenney.net/css/document.css
xhtml header:		<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML"></script>
latex input:		mmd-natbib-plain
latex input:		mmd-article-begin-doc
latex footer:		mmd-memoir-footer

* What about making the simplest possible tracker-based p2p network?
* I don't have any idea what I'm doing... WIN.
* First, I'm going to get 2 clients to each send half of a file to a 3rd client.
* Then, I'll figure out where to take it from there.
    * DHT, swappable algorithms, secure connection, secure transfer,
      encrypted file chunks, optimizations, etc.

## The Basic Layout

### Tracker

* Has lists of peers seeding and leeching for each file
* In the first, simplest iteration, it will just direct you to the seeders and
  initiate a download from them
    * E.g. 1/2 from one guy 1/2 from the other
* The `DHT` can come later, let's just get the files *distributed* first

## References

1. **[Computer Networks Class Project](https://github.com/ethanp/p2p-downloader)**
   -- *Javier Figueroa*
2. **Java Network Programming** -- *Elliotte Rusty Harold* (O'Reilly)
3. The sample Chat Client from Ray Toal at Loyola Marymount Univ. (Catholic, in West LA)

        http://cs.lmu.edu/~ray/notes/javanetexamples/#chat
    which I have as an IntelliJ project somewhere
4. When the time comes, there's a bunch of great-looking papers in the
   [*Harvard* **P2P Systems** Course](http://www.eecs.harvard.edu/~mema/courses/cs264/cs264.html)
