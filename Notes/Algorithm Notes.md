Sorting algorithms
==================

Insertion sort
--------------
**5/27/14**

### Properties

* A **stable** sort
* Efficient for *very* small data sets
* Faster than other O(n^2) algorithms (**selection** & **bubble** sort)
* **In-place**
* Efficient for data sets already nearly sorted

### How to do

1. Take `elem[1]`, if it's smaller than `elem[0]`, swap them
2. Take `elem[2]`, if it's smaller than `elem[0]`, move `elem[0:1]` over
   to the right, and put it at the front
3. Continue in this manner, moving along the array, putting each
   element in its proper place in relation to those elements previously seen
   
Selection sort
--------------
**5/27/14**

### Properties

* Painfully **slow**, comfortingly **simple**
* Similar to **insertion sort**, but slower
* **In-place**

### How to

1. Scan the array for the smallest element, and put it in slot `0`
2. Scan the *rest* of the array for its smallest element, and put it in slot `1`
3. And so on.
   
Bubble sort
-----------
**5/27/14**

* Repeatedly step through the list to be sorted, comparing each
  pair of adjacent items and swap them if they are in the wrong order
* Keep repeating this until no swaps are needed
* The algorithm gets its name from the way smaller elements "bubble"
  to the top of the list
* The *only* thing good about it is that it only requires one pass
  if the list is already sorted
* It is a **stable** sort and **in-place**


Radix Sort
----------
**5/27/14** [Radix sort on Wikipedia](http://en.wikipedia.org/wiki/Radix_sort)

* Sorts data with integer keys by grouping keys by the individual digits which share the same significant position and value.
* **In English**:
    1. Clump/group/bucket given integers by least/most significant digit
    2. Clump/group/bucket each of those by next least/most significant digit
    3. Repeat for a number of times equal to the length of the longest key
* Dates back to 1887
* Can be used for strings (because those can be rewritten as integers)
* Most significant digit (MSD) / Least significant digit (LSD) varieties

### Implementations

Gazillions of options

* Iterative version with Queues (3 pass)
* Recurse into the different buckets (can be done in parallel)
* In-place
* Stable version (with size *n* buffer)
* Hybrids -- e.g. switch to **insertion sort** when the buckets get small
* Incremental trie-based -- create a trie then do depth-first, in-order traversal

Spaghetti Sort
--------------
**5/27/14** [Found on Wikipedia](http://en.wikipedia.org/wiki/Spaghetti_sort)

1. Gather all the spaghetti into a bundle
2. Put it vertically on the table and let all strands rest on the table
3. While (there is more spaghetti)
    1. Lower your hand slowly onto the spaghetti
    2. Pull out the first strand your hand touches


Other algorithms
================

Bloom Filter
------------
See `Data Structures Notes.md`


Vocab
=====

* **Stable sort** -- preserves order of duplicate entries