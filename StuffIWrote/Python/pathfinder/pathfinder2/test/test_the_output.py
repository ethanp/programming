# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# Use the sample directory structure to verify
# that the DSL produces the correct output.

import unittest, os
from pathfinder2.dsl import pathfinder as p2
import create_test_structure as cts

class TestBasicStructure(unittest.TestCase):
    def setUp(self):
        cts.build_basic_structure()

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

    def test_20_total(self):
        found_files = p2.pathfinder('structure', ['*', '*'])
        self.assertFalse(not found_files, 'returned None instead of 10 files')
        self.assertEqual(len(found_files), 20, 'did not find 20 files')
        for f in found_files:
            self.assertRegexpMatches(f, r'.txt|.csv', 'found file of incorrect type')


class TestComplexStructure(unittest.TestCase):
    def setUp(self):
        cts.build_more_complex_structure()

    def test_ignore_dir_1(self):
        a = os.listdir('.')
        found_files = p2.pathfinder('structure', ['*','0','*'])
        self.assertFalse(not found_files, 'returned None instead of 10 files')
        self.assertEqual(len(found_files), 20, 'did not find 20 files')
        for f in found_files:
            self.assertRegexpMatches(f, r'data|readIt', "didn't find required file")
            self.assertNotRegexpMatches(f, r'ignore', "found file that should've been ignored")


if __name__ == '__main__':
    unittest.main()
