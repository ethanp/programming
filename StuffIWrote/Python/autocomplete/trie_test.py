'''
8/8/14, 3:56 AM
Ethan Petuchowski
trie_test

unittests of Trie in trie.py
'''
import unittest
from trie import Trie

class TrieTest(unittest.TestCase):
    def setUp(self):
        self.trie = Trie()

    def assertOrderlessArrayEquality(self, a, b, msg=None):
        self.assertEqual(sorted(a), sorted(b), msg)

    def test_insert_and_get_word_counts(self):
        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            self.trie.get_word_counts(),
            [('a', 1)])

        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            self.trie.get_word_counts(),
            [('a', 2)])

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            self.trie.get_word_counts(),
            [('a', 2), ('b', 1)])

        self.trie.insert('ab')
        self.assertOrderlessArrayEquality(
            self.trie.get_word_counts(),
            [('a', 2), ('b', 1), ('ab', 1)])

        self.trie.insert('abcdefg')
        self.assertOrderlessArrayEquality(
            self.trie.get_word_counts(),
            [('a', 2), ('b', 1), ('ab', 1), ('abcdefg', 1)])

    def test_get_top_k_with_prefix(self):
        self.trie.insert('a')
        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix(''),
            [('a', 1)])

        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix('a'),
            [('a', 1)])

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix(''),
            [('a', 1), ('b', 1)])

        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix('a'),
            [('a', 1)])

        self.trie.insert('asdf')
        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix('a'),
            [('a', 1), ('asdf', 1)])

        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix('as'),
            [('asdf', 1)])

        self.trie.insert('b')
        self.assertOrderlessArrayEquality(
            self.trie.get_top_k_with_prefix(prefix='', k=1),
            [('b', 2)])

if __name__ == '__main__':
    unittest.main()
