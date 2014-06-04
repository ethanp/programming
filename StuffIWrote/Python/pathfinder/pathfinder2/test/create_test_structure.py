# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# create a sample directory structure for
# testing the correctness of pathfinder2

import os
import sys
import shutil

STRUCTURE_DIR = '/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/Python/pathfinder/pathfinder2/test'
STRUCTURE_LOC = './structure'

SAMPLE_CSV = '''
1,comma,2,comma,3,little indians,
4,comma,5,comma,6,little indians,
'''[1:]

SAMPLE_TXT = '''
this is my text
on the computer
'''[1:]

def re_establish(loc):
    if os.path.isdir(loc):
        shutil.rmtree(loc)
    os.mkdir(loc)

def create_dirs_and_fill_them():
    """
    create 10 folders and fill each with a
    uniquely named sample .txt and sample.csv
    """
    for i in range(10):
        os.mkdir(str(i))
    for file_num, d in enumerate(os.listdir('.')):
        os.chdir(d)
        with open('readIt'+str(file_num)+'.txt', 'w+') as out_txt:
            out_txt.write(SAMPLE_TXT)
        with open('data'+str(file_num)+'.csv', 'w+') as out_csv:
            out_csv.write(SAMPLE_CSV)
        os.chdir('..')

def rebuild_test_structure():
    # go to *this* location
    os.chdir(STRUCTURE_DIR)
    re_establish(STRUCTURE_LOC)
    os.chdir(STRUCTURE_LOC)
    create_dirs_and_fill_them()
    os.chdir('..')

if __name__ == '__main__':
    rebuild_test_structure()
