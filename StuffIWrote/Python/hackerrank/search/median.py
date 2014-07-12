'''
7/12/14, 1:13 PM
Ethan Petuchowski
median

this was pretty cool.
I had it right from the start,
but had tons of bugs to iron out.

https://www.hackerrank.com/challenges/find-median
'''

from random import randint


def partition(lst, piv, left, right):
    ''' returns new piv loc '''

    # primary swap
    lst[right], lst[piv] = lst[piv], lst[right]

    # iterate through
    piv_loc = i = left
    while i < right:
        if lst[i] < lst[right]:
            lst[i], lst[piv_loc] = lst[piv_loc], lst[i]
            piv_loc += 1
        i += 1
    lst[piv_loc], lst[right] = lst[right], lst[piv_loc]
    return piv_loc

def median(lst, left, right):
    ind = randint(left, right)
    piv_loc = partition(lst, ind, left, right)
    mid = len(lst) / 2 # heh...
    if piv_loc == mid:
        return lst[piv_loc]
    elif piv_loc < mid:
        return median(lst, piv_loc+1, right)
    else:
        return median(lst, left, piv_loc-1)

def main():
    n = input()
    arr = map(int, raw_input().split())
    print median(arr, 0, len(arr) - 1)

if __name__ == '__main__':
    main()
