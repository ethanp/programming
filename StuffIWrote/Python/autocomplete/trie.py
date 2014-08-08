'''
8/7/14, 6:06 PM
Ethan Petuchowski
Trie

A description in here
'''
import operator

from heap import HeapElem, MaxHeap

COUNT, CHILDREN = range(2)

class Trie(object):
    '''
    I'm not sure what these are supposed to look like,
    so I'm making mine up as I go along

    It can only hold strings

    The plan: it's just a giant nest of hashmaps
    Realization: it should probably just be a giant nest of Tries (do'h!)

    For example:

    a->d->d->e->r
     \
      \->r->e

    Is represented as:

    Trie({'a' :
        (50, {'d' :
            (10, {'d' :
                (30, {'e' :
                    (0, {'r' :
                        (2, {})
                    })
                })
            })
        , 'r' :
            (0, {'e' :
                (40, {})
            })
        })
    })

    A more efficient representation might use an array of all the letters
    instead of a hashmap, but let's stick with this for now.
    '''
    def __init__(self):
        self.base = {}

    def insert(self, in_string):
        '''
        adds it if it doesn't exist in the structure
        adds to the counter if it does exist already
        '''
        if not in_string:
            return

        curr_hash = self.base

        # go to the spot
        for c in in_string[:-1]:
            if c not in curr_hash:
                curr_hash[c] = [0, {}]
            curr_hash = curr_hash[c][CHILDREN]

        # update the counter
        c = in_string[-1]
        if c not in curr_hash:
            curr_hash[c] = [1, {}]
        else:
            curr_hash[c][COUNT] += 1

    def print_word_counts(self):
        '''
        print all the words contained in the trie
        via depth-first traversal
        '''
        curr_hash = self.base
        if curr_hash:
            for i in self._get_word_counts(curr_hash, '', []):
                print i
        else:
            print 'This trie is totally empty'

    def get_word_counts(self):
        word_counts = self._get_word_counts(self.base, '', [])

        # this sorting by this then that doesn't seem to work...
        # it does sort by count, but it doesn't seem to then sort lexicographically
        sorted_counts = sorted(word_counts, key=operator.itemgetter(1, 0), reverse=True)

        return sorted_counts

    def _get_word_counts(self, curr_hash, curr_str, curr_arr):
        for ch, (count, next_hash) in curr_hash.items():
            if count > 0:
                curr_arr.append((curr_str+ch, count))
            if next_hash:
                curr_arr += self._get_word_counts(next_hash, curr_str+ch, [])
        return curr_arr

    def get_top_k_with_prefix(self, prefix, k=5):
        if not self.base:
            print 'This trie is totally empty'
            return

        curr_hash = self.base
        for ch in prefix[:-1]:
            if ch in curr_hash:
                curr_hash = curr_hash[ch][CHILDREN]
            else:
                print prefix, 'is not in this trie'
                return []

        arr = []
        if prefix:
            ch = prefix[-1]
            if ch in curr_hash:
                count = curr_hash[ch][COUNT]
                if count > 0:
                    arr.append((prefix, count))
                curr_hash = curr_hash[ch][CHILDREN]

        words_counts = self._get_word_counts(curr_hash, prefix, arr)
        sorted_counts = sorted(words_counts, key=operator.itemgetter(1, 0), reverse=True)
        k_sorted_counts = sorted_counts[:k]
        return k_sorted_counts

if __name__ == '__main__':
    pass
