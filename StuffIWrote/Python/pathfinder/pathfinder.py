# coding=utf-8

# 3/31/14
# Ethan Petuchowski

import os

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
    """
    1st thing must be a path we can `cd into`

    next things follow the DSL:
        *    means any file
        ^... means "startswith"
        ...$ means "endswith"
        ...= means "is_not_dir"
    """

    if len(path) == 0 or os.path.isdir(cd_able):
        return [os.path.abspath(cd_able)]

    os.chdir(cd_able)

    head, tail = path[0], path[1:]
    starts_with, ends_with, is_file = False, False, False

    if head[0] == '*':
        return flatten([pathfinder(os.path.abspath('./'+f), tail)
                        for f in os.listdir('.')])

    if head[0] == '^':
        starts_with = True
        head = head[1:]

    if head[-1] == '=':
        is_file = True
        head = head[1:]

    if head[-1] == '$':
        ends_with = True
        head = head[:-1]

    starts_with = head if starts_with else ''
    ends_with = head if ends_with else ''

    return flatten([pathfinder(os.path.abspath(p), tail)
                    for p in os.listdir('.')
                    if head in p
                    and p.startswith(starts_with)
                    and p.endswith(ends_with)
                    and os.path.isdir(p) != is_file])


if __name__ == '__main__':
    pathfinder(['/Users/ethan/Desktop', ['New Files', '*', '^Something', '*', '.csv$=']])
