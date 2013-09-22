# 9/21/2013

from collections import Counter

def isPermutation(a, b):
    aCtr = letterCounter(a)
    bCtr = letterCounter(b)
    if not aSubCtrB(aCtr, bCtr) or \
       not aSubCtrB(bCtr, aCtr):
        print False
        return False
    print True
    return True

def letterCounter(a):
    aCtr = Counter()
    for chr in a:
        aCtr[chr] += 1
    return aCtr

def aSubCtrB(aCtr, bCtr):
    for chr, ct in bCtr.items():
        if chr not in aCtr.keys() or aCtr[chr] != ct:
            return False
    return True

#======================================================
a = "ypaph"
b = "happy"
c = "yaph"
isPermutation(a, b)
isPermutation(a, c)