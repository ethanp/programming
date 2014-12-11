'''
12/10/14, 6:59 PM
Ethan Petuchowski
basics.py

A first trial of multi-process-ing in Python

1. Let's say I have a gigantic array of 1_000_000 ints
2. I want to square each one
3. So split the array into 8 pieces (1 for each processor)
4. And square each element in the split
5. Then join the results back into a single array

well as it turned out there was an example similar to that in the docs,
which I have basically just replicated in here.

When I run it, the Activity Monitor indicates that 4 processors are in
use (i.e. success!).

'''

from multiprocessing import Pool

def f(x):
    return x*x

if __name__ == '__main__':
    pool = Pool(4)
    result = pool.map(f, range(100000))
    print 'done', len(result)
