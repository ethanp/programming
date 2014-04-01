# coding=utf-8

# 3/31/14
# Ethan Petuchowski

import os

def flatten(given):
    """
    e.g.

        flatten([1,[2,3,4,[[5,6],7,8],9],[]])

                ==>>  [1,2,3,4,5,6,7,8,9]
    """
    to_ret = []
    for l in given:
        if isinstance(l, list):
            to_ret += flatten(l)
        else:
            to_ret.append(l)
    return to_ret

def test_flatten():
    print flatten([1,2,3,4])
    print flatten([1,[2,3,4,[[5,6],7,8],9],[]])


def pathfinder(cd_able, path):
    """
    @param  `cd_able`  either a path we can `cd into` or not a directory
    @param  `path`  a list where each element follows the DSL given below
    @return:  absolute path to all the matched files

    The DSL:
        *     means any file
        ^...  means "startswith"
        ...   means "contains"
        ...$  means "endswith"
        ...=  means "is_not_dir"
        ...$= means "endswith and is_not_dir"
        ...=$ means "endswith an '='" (i.e. = directive must be after desired $ directive)

    e.g. `file_paths = pathfinder('/Users/ethan/Desktop/New Freqs', ['*', '*', '^Other_', '^Transform='])`
    """
    if len(path) == 0 or not os.path.isdir(cd_able):
        l = [os.path.abspath(cd_able)]
        if os.path.isdir(cd_able):
            os.chdir(cd_able)
        if len(path) == 0:
            os.chdir('..')
        return l

    os.chdir(cd_able)
    head, tail = path[0], path[1:]
    starts_with, ends_with, is_file = False, False, False

    if head == '*':
        l = []
        for f in os.listdir('.'):
            if '.DS_Store' not in f:
                l += pathfinder(f, tail)
                os.chdir('..')
        return flatten(l)

    if head[0] == '^':
        starts_with = True
        head = head[1:]

    if head[-1] == '=':
        is_file = True
        head = head[:-1]

    if head[-1] == '$':
        ends_with = True
        head = head[:-1]

    starts_with = head if starts_with else ''
    ends_with = head if ends_with else ''

    l = flatten([pathfinder(p, tail)
                    for p in os.listdir('.')
                    if head in p
                    and '.DS_Store' not in p
                    and p.startswith(starts_with)
                    and p.endswith(ends_with)
                    and os.path.isdir(p) != is_file])
    return l


if __name__ == '__main__':
    files = pathfinder('/Users/ethan/Desktop', ['036 Vibe Data', '^KGB', '*', '*', '^Other'])
    for f in files: print f
