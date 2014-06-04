# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# Use the sample directory structure to verify
# that the DSL produces the correct output.

import unittest
from pathfinder2.dsl import pathfinder as p2
import create_test_structure as cts


def find_csvs():
    cts.rebuild_test_structure()
    found_files = p2.pathfinder('.', ['*','.csv'])
    if not len(found_files) == 10:
        print '10 files were NOT found'
    else:
        print 'found 10 files'

