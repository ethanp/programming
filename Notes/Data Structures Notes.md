Basic Data Structures
=====================

Map
---

### HashMap

### TreeMap


Array
-----

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

### Red-Black Tree

[Tim Roughgarden's Coursera lecture on it](https://www.youtube.com/watch?v=4slgC3UOXc0)

* A form of *balanced* binary search tree with additional imposed **invariants**
  to ensure that all the common operations (insert, delete, min, max, pred,
  succ, search) happen in O(log(n)) time.
  
#### Invariants

1. Each node is red or black
2. Root is black
3. A red node must have black children
4. Every `root->NULL` path through the tree has same number of black nodes

Theorem: every red-black tree with *n* nodes has height ≤ 2log_2(n+1)

#### Implementations

This is what backs Java's `TreeMap<T>`

Heap
----

### MinHeap

### MaxHeap

Queue
-----

### Java implementations

1. `LinkedList<T>`
2. `PriorityQueue<T>`

### Java methods

1. `add()`
2. `remove()`


Other
-----

### LRUCache


More Advanced Data Structures
=============================

### Red Black Tree

### B+ Tree

### RingBuffer

### Trie

### LinkedHashMap

