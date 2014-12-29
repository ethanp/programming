# Simple-P2P

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

#### Attributes

* Get seeders for file

#### Methods

* List known files

### Peer

#### Attributes

* `List<P2PFile>`

#### Methods

* Create or Join track-group for given File

### P2PFile

#### Attributes

* Size
* Chunk-Size
* Tracker `URL:PORT`

#### Methods

* `P2PFile("myfile.txt")`
