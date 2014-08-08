'''
8/8/14, 3:56 AM
Ethan Petuchowski
trie_test

unittests of Trie in trie.py
'''
import unittest
from collections import Counter
import re
import operator
from trie import Trie

ALICE_TXT = '/Users/Ethan/code/personal_project_use/libraries_to_use/' \
            'Java/guava-libraries/guava-tests/test/com/google/common/' \
            'io/testdata/alice_in_wonderland.txt'

class TrieTest(unittest.TestCase):
    def setUp(self):
        self.trie = Trie()

    def assertOrderlessArrayEquality(self, truth, mine, msg=None):
        self.assertEqual(set(truth), set(mine), msg)

    def test_insert_and_get_word_counts(self):
        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            [('a', 1)],
            self.trie.get_word_counts())

        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            [('a', 2)],
            self.trie.get_word_counts())

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            [('a', 2), ('b', 1)],
            self.trie.get_word_counts())

        self.trie.insert('ab')
        self.assertOrderlessArrayEquality(
            [('a', 2), ('b', 1), ('ab', 1)],
            self.trie.get_word_counts())

        self.trie.insert('abcdefg')
        self.assertOrderlessArrayEquality(
            [('a', 2), ('b', 1), ('ab', 1), ('abcdefg', 1)],
            self.trie.get_word_counts())

    def test_get_top_k_with_prefix(self):
        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            [('a', 1)],
            self.trie.get_top_k_with_prefix(''))

        self.assertOrderlessArrayEquality(
            [('a', 1)],
            self.trie.get_top_k_with_prefix('a'))

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            [('a', 1), ('b', 1)],
            self.trie.get_top_k_with_prefix(''))

        self.assertOrderlessArrayEquality(
            [('a', 1)],
            self.trie.get_top_k_with_prefix('a'))

        self.trie.insert('asdf')
        self.assertOrderlessArrayEquality(
            [('a', 1), ('asdf', 1)],
            self.trie.get_top_k_with_prefix('a'))

        self.assertOrderlessArrayEquality(
            [('asdf', 1)],
            self.trie.get_top_k_with_prefix('as'))

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            [('b', 2)],
            self.trie.get_top_k_with_prefix(prefix='', k=1))

    def test_top_k_using_alice(self):
        f = open(ALICE_TXT)
        alice = f.read()
        f.close()
        words = map(lambda x: re.sub('\W', '', x).lower(), alice.split())
        words = filter(lambda x: x != '', words)
        map(lambda x: self.trie.insert(x), words)

        # there are exactly 298 words with > 12 instances
        # if we go more, we might pick different ones than the Counter
        self.assertOrderlessArrayEquality(
            Counter(words).most_common(298),
            self.trie.get_top_k_with_prefix('', 298))

if __name__ == '__main__':
    unittest.main()
