# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# Use the sample directory structure to verify
# that the DSL produces the correct output.

import unittest
from pathfinder2.dsl import pathfinder as p2
import create_test_structure as cts

class TestPathfinder(unittest.TestCase):
    def setUp(self):
        cts.rebuild_test_structure()

    def test_10_base_dirs(self):
        found_files = p2.pathfinder('structure', ['*'])
        self.assertFalse(not found_files, 'returned None instead of 10 files')
        self.assertEqual(len(found_files), 10, 'did not find 10 files')

    def test_10_csvs(self):
        found_files = p2.pathfinder('structure', ['*', '.csv$'])
        self.assertFalse(not found_files, 'returned None instead of 10 files')
        self.assertEqual(len(found_files), 10, 'did not find 10 files')
        for f in found_files:
            self.assertTrue(f.endswith('.csv'), 'found non-csv file')

if __name__ == '__main__':
    unittest.main()
