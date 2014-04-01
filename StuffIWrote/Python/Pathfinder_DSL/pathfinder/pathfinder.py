# coding=utf-8

# 3/31/14
# Ethan Petuchowski

import os

# 1st thing must be a path we can `cd into`
#
# next things follow the dsl:
# * means any file
# ^... means "startswith"
# ...$ means "endswith"
# ...= means "is_not_dir"
from nltk import flatten

def flatten(given):
    """
    Flatten a given list.
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
    os.chdir(cd_able)
    if len(path) == 0:  return [os.path.abspath(cd_able)]
    head = path[0]
    tail = path[1:]
    starts_with, ends_with = False, False

    if head[0] == '*':
        return flatten([pathfinder(os.path.abspath('./'+f), tail) for f in os.listdir('.')])

    if head[0] == '^':
        starts_with = True
        head = head[1:]

    if head[-1] == '$':
        ends_with = True
        head = head[:-1]

    starts_with = head if starts_with else ''
    ends_with = head if ends_with else ''

    return flatten([pathfinder(os.path.abspath(p), tail)
                    for p in os.listdir('.')
                    if p.startswith(starts_with)
                    and p.endswith(ends_with)])


if __name__ == '__main__':
    test_flatten()
