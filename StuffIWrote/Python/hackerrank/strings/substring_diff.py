'''
7/12/14, 10:05 PM
Ethan Petuchowski
substring_diff

https://www.hackerrank.com/challenges/substring-diff
'''

"""
this was the original brute-force way, and it was "too slow"
"""

def diffs(a, i, b, j, L):
    c = 0
    for k in range(L):
        if a[i+k] != b[j+k]:
            c += 1
    return c

def loop(a, b, S):
    n = len(a)
    for sub_len in reversed(range(n + 1)):
        for i in range(n - sub_len + 1):
            for j in range(n - sub_len + 1):
                if diffs(a, i, b, j, sub_len) <= S:
                    return sub_len
    return -1


"""
this was my (incorrect) first attempt at a faster method for computing this:
we compute a table of differences, then we can just sum up
different subsets of that table
"""

SAME, DIFF, ERR = range(3)

def precompute_diffs(a, b):
    """
    make a table where rows are i's and cols are j's
    and values are 1 if they're diff, 0 otw
    hell
    doll
    i\j 0 1 2 3
      0 1 1 0 0
      1 1 1 0 2
      2 1 1 2 2
      3 1 2 2 2
    """
    table = [[ERR]*len(b) for _ in range(len(a))]
    for i in range(len(a)):
        for j in range(len(b)-i):
            if a[i] == b[j]:
                table[i][j] = SAME
            else:
                table[i][j] = DIFF
    return table

def quick_loop(a, b, S, table):
    for sub_len in reversed(range(len(a)+1)):
        for i in range(len(a)-sub_len+1):
            for j in range(len(a)-sub_len+1):
                diff = sum(table[i][j:j+sub_len])
                if diff < S:
                    return sub_len


"""
this is my second attempt to speed this baby up:
gonna use a hash-table and a recurrence-relation that I just invented
(getting fancy, praying for the best...)
"""

# it's i x j x L
hash_table = {}
def init_hash_table(a, b):
    for i in xrange(len(a)):
        for j in xrange(len(b)):
            hash_table[(i, j, 1)] = 0 if a[i] == b[j] else 1

def lookup(i, j, L):
    if (i, j, L) in hash_table:
        return hash_table[(i, j, L)]
    else:
        # here's my stroke of brilliance!
        """
        first shot here used simple recursion:
            val = lookup(i, j, L-1) + lookup(i+L-1, j+L-1, 1)
            hash_table[(i, j, L)] = val
            return val

        However:
            RuntimeError: maximum recursion depth exceeded (FOILED AGAYNE!@!)
        """
        """
        perhaps I need to do an iteration instead of a recursion:
            for k in range(2, L+1):
                hash_table[(i, j, k)] = hash_table[(i, j, k-1)] + hash_table[(i+k-1, j+k-1, 1)]
            return hash_table[(i, j, L)]

        However:
            it is still *correct*, and now it doesn't hit recursion limits...
            but the hash_table just gobbles up all my RAM and it doesn't finish computing.

        I'm not sure what to try next, I'll probably have to look up what to do in this situation....
        """
        for k in xrange(2, L+1):
            hash_table[(i, j, k)] = hash_table[(i, j, k-1)] + hash_table[(i+k-1, j+k-1, 1)]
        return hash_table[(i, j, L)]

def hash_loop(a, b, S):
    for L in reversed(xrange(1,len(a)+1)):
        for i in xrange(len(a)-L+1):
            for j in xrange(len(b)-L+1):
                diff = lookup(i, j, L)
                if diff <= S:
                    return L

def main():
    global hash_table
    N = input()
    for _ in range(N):
        S, a, b = raw_input().split()
        S = int(S)
        hash_table = {}
        init_hash_table(a, b)
        print hash_loop(a, b, S)

if __name__ == '__main__':
    main()
