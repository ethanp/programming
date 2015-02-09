latex input:        mmd-article-header
Title:              Java Collections Notes
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

## The way this works

For all the "optional" methods, it means that if you're not implementing it,
you should throw an `UnsupportedOperationException`. This sounds like a sloppy
way to set up the Collections hierarchy, but the point was to make there be
only a few simple interfaces so that it's easier to learn.

### Iterator

    interface Iterator<E> {
        boolean hasNext()
        E next()

        /* [optional]
         * removes most recent element
         * you *must* have just called next() or previous()
         * can't call it twice in a row
         */
        void remove()
    }

#### ListIterator

These can be extracted from a `List<E>`

    interface ListIterator<E> {
        int nextIndex()
        boolean hasPrevious()
        E previous()
        int previousIndex()
        void add(E)             // optional
        void set(E)             // optional
    }

### Iterable

Implementing `Iterable<T>` allows you to use the "foreach" loop construct.

    interface Iterable<T> {
        Iterator<T> iterator()
    }

### Collection

"The root interface in the *collection heirarchy*." A lot of freedom is left
up to the implementer to provide whatever specifics they want.

    interface Collection<E> extends Iterable<E> {
        boolean add(E)                             // optional
        boolean addAll(Collection<? extends E>)    // optional
        void clear()                               // optional
        boolean contains(Object)
        boolean containsAll(Collection<?>)
        boolean equals(Object)
        int hashCode()
        boolean isEmpty()
        Iterator<E> iterator()
        boolean remove(Object)                     // optional
        boolean removeAll(Collection<?>)           // optional
        int size()
        Object[] toArray()
        <T> T[] toArray(T[] a) // you can specify runtime type of array
    }


### List

An *ordered* collection (aka. "sequence"), meaning **it has indexes**.

    interface List<E> extends Collection<E> {
        void add(int, E)
        boolean addAll(int, Collection<>)
        E get(int)
        int indexOf()
        int lastIndexOf(Object)
        ListIterator<E> listIterator()
        ListIterator<E> listIterator(int)
        boolean retainAll(Collection<?>)
        E set(int, E)                       // optional
        List<E> subList(int, int)
    }

### AbstractCollection

Implements most of `Collection` based on (`Iterator<E> iterator()`) and (`int
size()`), so that those are *all that remain* for *you* to get your class to
`implement Collection`.

This example is for `contains(Object)`. Note that you can always override this
implementation if you want to.

    abstract class AbstractCollection<E> implements Collection<E> {
        public boolean contains(Object obj) {
            for (E element : this) // calls iterator()
                if (element.equals(obj))
                    return true;
            return false;
        }
    }

### Map

From the docs:

* An object that maps keys to values
* A map cannot contain duplicate keys
* Each key can map to at most one value

Doesn't extend anything. Comments are mine.

    interface Map<K, V> {

        /* find & retrieve */
        boolean             containsKey(Object)
        boolean             containsValue(Object)
        V                   get(Object key)

        /* size */
        int                 size()
        boolean             isEmpty()

        /* alternate view */
        Set<K>              keySet()
        Collection<V>       values()

        // from this you call entry.getKey|Value()
        Set<Map.Entry<K,V>> entrySet()

        boolean             equals(Object)
        int                 hashCode()

        /* insert */
        // overwrites, returns *previous* value
        V                   put(K, V)      // optional
        void                putAll(Map<>)  // optional

        /* remove */
        void clear()        // optional
        V remove(Object)    // optional
    }

### HashMap
* **no guarantees** about iteration order

### NavigableMap (interface)

 * A `SortedMap`  which allows searches for "closest matches"
    * `lowerEntry`, `floorEntry`, `ceilingEntry`, `higherEntry`,
      \\(\Longrightarrow\\) `Map.Entry<K,V>`
    * `lowerKey`, ..., \\(\Longrightarrow\\) `K`

* **can be traversed in either direction**.
* The `descendingMap` method returns a view of the map with the senses of all
  relational and directional methods inverted.
* Iterates/sorted according to `compareTo()` (or supplied `Comparator`)

### TreeMap
* It's **a Red-Black tree based `NavigableMap`** (above)

### LinkedHashMap

* **Iterates in order of insertion**
* Contains both a hashmap *and* a **double-linked-list** in ordered by
  *insertion-order*
    * In other words, when you call `map.entrySet()` the *first* one out is
      the *first* one you *inserted*
* If you (re-)insert a `key` that the map already `contains`, the order is
  *not* affected

[SO Maps]: http://stackoverflow.com/questions/2889777
