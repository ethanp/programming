# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# pathfinder in pathfinder2
# a reimplementation of the buggy original pathfinder
# with better testing and better implementation
# see readme.md for description of DSL

import os

# one implementation option: BreadthFirstSearch
#   store a queue of (index_in_find, abs_path)
#   then go through it at the find[index_in_find+1] level

# another option: DepthFirstSearch
#   I'd need to think about this one

def pathfinder(cd_here, find):
    os.chdir(cd_here)
    for elem in find:
        found = []
        start_string, not_dir, end_string, asterisk = [False] * 4

        if not isinstance(elem, str): # note it's 'str', not 'string'
            raise TypeError('path elems must be strings')

        if elem == '*':
            asterisk = True

        if elem.startswith('^'):
            start_string = True
            elem = elem[1:]

        if elem.endswith('='):
            not_dir = True
            elem = elem[:-1]

        if elem.endswith('$'): # must be after '='
            end_string = True
            elem = elem[:-1]

        for f in os.listdir('.'):

            if start_string and not f.startswith(elem):
                continue

            if end_string and not f.endswith(elem):
                continue

            if not_dir and os.path.isdir(f):
                continue

            if elem not in f and not asterisk:
                continue

            found.append(os.path.abspath(f))

        return found
