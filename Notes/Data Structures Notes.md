latex input:    mmd-article-header
Title:          Data Structures Notes
Author:         Ethan C. Petuchowski
Base Header Level:  1
latex mode:     memoir
Keywords:       Data Structures, Algorithms
CSS:            http://fletcherpenney.net/css/document.css
xhtml header:   <script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
</script>
copyright:      2014 Ethan C. Petuchowski
latex input:    mmd-natbib-plain
latex input:    mmd-article-begin-doc
latex footer:   mmd-memoir-footer


# Basic Data Structures

## Map

### HashMap

### TreeMap


## Array

### ArrayList

### LinkedList


Set
---

### HashSet

### TreeSet


Tree
----

* **Perfect** -- every level is full

               x
             /   \
            /     \
           x       x
          / \     / \
         x   x   x   x
        / \ / \ / \ / \
        x x x x x x x x

* **Complete** -- every level, except possibly the last,
                  is completely filled, and all nodes are
                  as far left as possible

               x
             /   \
            /     \
           x       x
          / \     / \
         x   x   x   x
        / \ /
        x x x

* **Full** -- every node other than the leaves has two children

               x
             /   \
            /     \
           x       x
          / \     / \
         x   x   x   x
        / \ / \
        x x x x

* **Height** -- *distance* from root to deepest leaf
    * So for the above tree examples, the *height* is **3** (*not* 4)

### Binary Tree

### Binary Search Tree

#### Red-Black Tree
(see below)

Heap
----
1. [Wikipedia](http://en.wikipedia.org/wiki/Heap_(data_structure))
2. [Algorithms -- Sedgewick, pg. 316]()
3. [Heapsort Summary Page](http://www.sorting-algorithms.com/heap-sort)

#### 5/4/14

* a binary heap is a **complete** binary tree which satisfies the heap
  ordering property.
* The ordering can be one of two types:
    1. the **min-heap** property: the value of each node is *greater than or
       equal* to the value of its parent, with the minimum-value element at
       the root.
    2. the **max-heap** property: same but flipped
* A heap is not a sorted structure but can be regarded as **partially
  ordered**.
    * There is no particular relationship among nodes on any given level, even
      among the siblings
* The heap is one maximally efficient implementation of a **priority queue**

#### 6/27/14

* the parent of the node at position `k` in a heap is at position `k/2`
* See implementation with explanation at
  `~/Dropbox/CSyStuff/PrivateCode/PreDraft_6-27-14`

Queue
-----

### Java implementations

1. `LinkedList<T>`
2. `PriorityQueue<T>`

### Java methods

1. `add`, `offer`
2. `remove`, `poll`
3. `peek`

### Deque -- double-ended queue

**Elements can be added/removed/inspected from either the front or the back.**

#### Implementations

* **Doubly-linked-list** -- all required operations are O(1), random access O(n)
* **Growing array** -- *amortized* time is O(1), random access O(1)

#### Java

##### Implementations

1. `LinkedList<T>` -- doubly-linked-list
2. `ArrayDeque<T>` -- growing array

##### Methods

Not sure why these are so strange...

* Insert at front -- `offerFirst`
* Insert at back -- `offerLast`
* Pop from front -- `pollFirst`
* Pop from back -- `pollLast`
* Peek at front -- `peekFirst`
* Peek at back -- `peekLast`


Other
-----

### LRUCache


More Advanced Data Structures
=============================

Bloom Filters
-------------

#### 5/4/14

[i-programmer](http://www.i-programmer.info/programming/theory/2404-the-bloom-filter.html)

### The Point

* **Bloom filters *attempt* to tell you if you have seen a particular data
  item before**
* **False-positives** are ***possible***, **false-negatives** are ***not***

#### Approximate answers are faster

* You can usually trade space for time; the more storage you can throw at a
  problem the faster you can make it run.
* In general you can also trade certainty for time.

### Applications

* *Google's BigTable database* uses one to reduce lookups for data rows that
  haven't been stored.
* The *Squid proxy server* uses one to avoid looking up things that aren't in
  the cache and so on

### History

* Invented in 1970 by Burton *Bloom*

### How it works

* Uses multiple different hash functions in-concert

#### Initialization

* A Bloom filter starts off with a **bit array** `Bloom[i]` initialized to
  zero.

#### Insertion

* To record each data value you simply compute *k* different hash functions
* Treat the resulting *k* values as indices into the array and set each of the
  *k* array elements to `1`.

#### Lookup

* Given an item to look up, apply the *k* hash functions and look up the
  indicated array elements.
* If any of them are zero you can be 100% sure that you have never encountered
  the item before.
* *However* even if all of them are one then you can't conclude that you have
  seen the data item before.
    * All you can conclude is that it is *likely* that you have encountered
      the data item before.

#### Removal

* **It is impossible to remove an item from a Bloom filter.**


### Characteristics

* As the bit array fills up the probability of a false positive increases
* There is a formula for calculating the optimal number of hash functions to
  use for a given size of the bit array and the number of items you plan to
  store in there
    * k_opt = 0.7(m/n)
* There is another formula for calculating the size to use for a given desired
  probability of error and fixed number of items using the optimal number of
  hash functions from above
    * m = -2n * ln(p)


Red Black Tree
--------------

[Tim Roughgarden's Coursera lecture on it](https://www.youtube.com/watch?v=4slgC3UOXc0)

* A form of *balanced* binary search tree with additional imposed
  **invariants** to ensure that all the common operations (insert, delete,
  min, max, pred, succ, search) happen in O(log(n)) time.

#### Invariants

1. Each node is red or black
2. Root is black
3. A red node must have black children
4. Every `root->NULL` path through the tree has same number of black nodes

Theorem: every red-black tree with *n* nodes has height ≤ 2log_2(n+1)

#### Implementations

This is what backs Java's `TreeMap<T>`

B+ Tree
-------

RingBuffer
----------

### Trie

### LinkedHashMap


TODO
----

Scala's `Vector` is a tree of arrays, kind of like a *B-Tree*, also like one
of the main *file formats*

