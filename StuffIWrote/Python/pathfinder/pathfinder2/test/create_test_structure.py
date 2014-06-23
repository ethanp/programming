# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# create a sample directory structure for
# testing the correctness of pathfinder2

import os
import sys
import shutil

# TODO replace with some way to get the current dir and go to '..'
TEST_DIR = '/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/Python/pathfinder/pathfinder2/test'
STRUCTURE_DIR = './structure'
CURRENT_DIR = '.'
PARENT_DIR = '..'

SAMPLE_CSV = '''
1,comma,2,comma,3,little indians,
4,comma,5,comma,6,little indians,
'''[1:] # remove leading newline

SAMPLE_TXT = '''
this is my text.
it is on the computer.
'''[1:]

def create_empty_dir(loc):
    """
    `rm -rf` the test structure in @loc if it currently exists,
    and `mkdir` an (as yet) empty folder to contain the new one
    """
    if os.path.isdir(loc):
        shutil.rmtree(loc)
    os.mkdir(loc)

def create_count_dirs(count):
    """ each is simply named {N} """
    for i in range(count):
        os.mkdir(str(i))

def insert_txt_to_dir(txt_name, given_dir):
    os.chdir(given_dir)
    with open(txt_name, 'w+') as out_txt:
        out_txt.write(SAMPLE_TXT)
    os.chdir(PARENT_DIR)


def insert_csv_to_dir(csv_name, given_dir):
    os.chdir(given_dir)
    with open(csv_name, 'w+') as out_csv:
        out_csv.write(SAMPLE_CSV)
    os.chdir(PARENT_DIR)

def insert_txt_and_csv(file_num, dir_name):
    insert_txt_to_dir('readIt' + str(file_num) + '.txt', dir_name)
    insert_csv_to_dir('data' + str(file_num) + '.csv', dir_name)

def create_dirs_and_fill_them(loc, count):
    """
    create @count folders in @dir, and fill each with
    a uniquely named readIt{N}.txt and data{N}.csv
    """
    create_count_dirs(count) # TODO these should be IN LOC I believe
    for file_num, dir_name in enumerate(os.listdir(loc)):
        insert_txt_and_csv(file_num, dir_name)

def create_different_files():
    os.mkdir("1")
    os.chdir("1")
    with open("file_to_ignore.txt", 'w+') as f:
        f.write("this file is different\nthen the other one")
    os.chdir(PARENT_DIR)


def build_basic_structure():
    """
    this one just has 10 dirs each with a .txt and .csv
    """
    os.chdir(TEST_DIR)
    create_empty_dir(STRUCTURE_DIR)
    os.chdir(STRUCTURE_DIR)
    create_dirs_and_fill_them(CURRENT_DIR, 10)
    os.chdir(PARENT_DIR)

def build_more_complex_structure():
    """
    the point of this one is that there is a directory with a file in it
    that should be ignored; either by ignoring the file, or the directory
    """
    os.chdir(TEST_DIR)
    create_empty_dir(STRUCTURE_DIR)
    os.chdir(STRUCTURE_DIR)
    create_count_dirs(10)
    for dir_name in os.listdir(CURRENT_DIR):
        os.chdir(dir_name)
        create_dirs_and_fill_them(CURRENT_DIR, 1)
        create_different_files()
        os.chdir(PARENT_DIR)
    os.chdir(PARENT_DIR)

if __name__ == '__main__':
    # build_basic_structure()
    build_more_complex_structure()
