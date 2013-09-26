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
    if newR: ret = ret  + newR
    return ret

#for t in testArrays:
#    print quickSort(t)


# 9/25/2013
# From Wikipedia (and Dany), this version is way more space-efficient
from random import randrange
def partition(array, left, right, pivIdx):
    pivVal = array[pivIdx]
    array[pivIdx], array[right] = array[right], array[pivIdx]  # move pivot to end
    storeIndex = left
    for i in range(left, right):
        if array[i] < pivVal:
            array[i], array[storeIndex] = array[storeIndex], array[i]

            # this is the hidden key moment: sI only increments when the condition
            # is met, but i *always* increments, so that's how the pivot ends up in
            # the right spot, where everything beneath it is less than it
            storeIndex += 1

    array[storeIndex], array[right] = array[right], array[storeIndex]
    return storeIndex

def inplaceQSort(array, left, right):
    if left < right:
        pivIdx = randrange(right-left+1) + left   # "inclusive"
        pivotNewIndex = partition(array, left, right, pivIdx)
        inplaceQSort(array, left, pivotNewIndex-1)
        inplaceQSort(array, pivotNewIndex+1, right)

def sortKickstarter(array):
    inplaceQSort(array, 0, len(array)-1)
    return array

print d
print sortKickstarter(d)
