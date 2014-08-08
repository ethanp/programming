'''
8/7/14, 6:05 PM
Ethan Petuchowski
Heap

*Was* for use in Autocomplete.
Actually I realized this is a silly way to get the top k.
Why not just maintain an rotating array of the top k [ O(kN) ], or if you
really care a (e.g. binary) tree [ O(N log(k)) ].
'''


# not positive I did the decorator syntax right
def heapElemOnly(fn):
    def wrapped(other):
        if isinstance(other, HeapElem):
            return fn()
        raise TypeError("Can't compare HeapElem with %s" % str(type(other)))
    return wrapped

class HeapElem(object):
    def __init__(self, word, count):
        self.word = word
        self.count = count

    # comparison operators
    @heapElemOnly
    def __lt__(self, other): return self.count < other.count
    @heapElemOnly
    def __gt__(self, other): return self.count > other.count
    @heapElemOnly
    def __eq__(self, other): return self.count == other.count
    @heapElemOnly
    def __le__(self, other): return self.count <= other.count
    @heapElemOnly
    def __ge__(self, other): return self.count >= other.count

    def __repr__(self): return '(%s, %d)' % (self.word, self.count)

class MaxHeap(object):
    '''
    * I believe this thing is an array of values,
      where it's children are known to be at indices (i*2, i*2+1).

    * This means we must Start the array at index (1),
        (i.e. this is where the Max lives)
      so that we can find a parent of any node at index (i/2).
    '''
    def __init__(self):
        # array[0] is a blank spot so that max lives at array[1], see docstring
        self.array = [None]

    def _swap(self, i, j):
        self.array[i], self.array[j] =\
            self.array[j], self.array[i]

    def insert(self, value):
        if not isinstance(value, HeapElem):
            print 'you can only insert HeapElems'
            return

        self.array.append(value)
        i = len(self.array)-1

        # is this what is known as "swim"?
        while i > 0:
            # swap while greater than parent
            if self.array[i/2] < self.array[i]:
                self._swap(i/2, i)
                i /= 2

            else: # done swapping
                break


    def top_k(self, k):
        return [self.pop() for _ in range(k)]

    def pop(self):
        # save current max
        to_ret = self.array[1]

        # move last element to top
        self.array[1] = self.array.pop()

        # "sink"? new top to its proper location
        # this is kind of sloppy code
        i = 1
        while i*2 < len(self.array):
            if i*2+1 < len(self.array):
                if self.array[i] < max(self.array[i*2], self.array[i*2+1]):
                    j = i*2 if self.array[i*2] > self.array[i*2+1] else i*2+1
                    self._swap(i, j)
                    i = j
                else:
                    break
            else:
                if self.array[i] < self.array[i*2]:
                    self._swap(i, i*2)
                    i *= 2
                else:
                    break

    def __repr__(self):
        s = ''
        for i, j in enumerate(self.array):
            s += "%d: %s\n" % (i, j)
        return s

if __name__ == '__main__':
    # h = MaxHeap()
    # h.insert(HeapElem('a', 1))
    # h.insert(HeapElem('e', 5))
    # h.insert(HeapElem('d', 4))
    # h.insert(HeapElem('b', 2))
    # h.insert(HeapElem('c', 3))
    # print h
    # print
    pass
