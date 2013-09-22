# 9/21/2013
# not randomized...
a = [5]
b = [25, 22]
c = [23, 62, 1, 673 ,1]
d = [10, 5, 15, 45, 2, 37]

testArrays = [a, b, c, d]

def quickSort(a):
    if len(a) < 2: return a
    piv = a[0]
    l = [e for e in a[1:] if e <= piv]
    r = [e for e in a[1:] if e > piv]
    newL = quickSort(l)
    newR = quickSort(r)
    ret = [piv]
    if newL: ret = newL + ret
    if newR: ret += newR
    return ret

for t in testArrays:
    print quickSort(t)