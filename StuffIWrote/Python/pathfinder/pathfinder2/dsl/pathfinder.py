# coding=utf-8
# 6/3/14
# Ethan Petuchowski
# pathfinder in pathfinder2
# a reimplementation of the buggy original pathfinder
# with better testing and better implementation
# see readme.md for description of DSL

import os

def pathfinder(cd_here, find):
    """
    BreadthFirstSearch: (going with this [for now])
      store a queue of (index_in_find, abs_path)
      then go through it at the find[index_in_find+1] level
    DepthFirstSearch:
      I'd need to think about this one
    """
    assert isinstance(cd_here, str), 'first param must be a string'
    assert isinstance(find, list), 'second param must be a list'
    assert len(find) > 0, 'second param must be nonempty'
    final_depth = len(find)-1
    queue = []
    depth = 0
    os.chdir(cd_here)  # returns None. bummer.

    def search(loc, elem):
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

        for f in os.listdir(loc):
            if start_string and not f.startswith(elem): continue
            if end_string and not f.endswith(elem):     continue
            if not_dir and os.path.isdir(f):            continue
            if not asterisk and elem not in f:          continue
            queue.append((depth, os.path.abspath(f)))

    search('.', find.pop(0))
    while find:
        locs = [name for file_depth, name in queue if file_depth == depth]
        depth += 1
        if locs:
            next_elem = find.pop(0)
            for location in locs:
                search(location, next_elem)
        else:
            find.pop(0)

    return [name for depth, name in queue if depth == final_depth]
