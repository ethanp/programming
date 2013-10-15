## 10/14/13
## Algorithm Design, Problem 6.4(c)

## to date, this was probably the most logically
##   challenging program I have ever written.

M = 10

N = [0, 15, 10, 10, 10]
S = [0,  5, 15, 15, 15]

# the best as far as I can tell, True means "in NY"
trueSeq = [None, True, False, False, False]

n = len(N)-1

seq = {}
OPT = {}
def computeOPT(i, inNY):
    # base case
    if i == 0:  return 0

    # to enable one to print resulting sequence of cities
    seq[i] = inNY

    # memoize (retrieval part)
    if (i, inNY) in OPT:  return OPT[(i, inNY)]

    # find current city's cost
    cityCost = N[i] if inNY else S[i]

    # new current total if we had to move to get here
    move = cityCost + computeOPT(i-1, not inNY) - M

    # new current total if we didn't have to move to get here
    noMove = cityCost + computeOPT(i-1, inNY)

    # memoize (storage part)
    OPT[(i, inNY)] = max(move, noMove)

    # return to caller
    return OPT[(i, inNY)]

print computeOPT(n, True)
print seq
OPT = {}
seq = {}
print computeOPT(n, False)
print seq


