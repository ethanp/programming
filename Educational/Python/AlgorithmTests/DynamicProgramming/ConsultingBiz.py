## 10/14/13
## Algorithm Design, Problem 6.4(c)

## to date, one of my most logically challenging programs

M = 10

N = [0, 15, 10, 10, 10]
S = [0,  5, 15, 15, 15]

# the best as far as I can tell, True means "in NY"
trueSeq = [None, True, False, False, False]

n = len(N)-1


OPT = {}
def computeOPT(i, inNY):
    """Recursive version"""
    # base case
    if i == 0:  return 0

    # memoize (retrieval part)
    if (i, inNY) in OPT:  return OPT[(i, inNY)]

    # find current city's cost
    cityCost = N[i] if inNY else S[i]

    # new current total if we had to move to get here
    move = computeOPT(i-1, not inNY) + M

    # new current total if we didn't have to move to get here
    noMove = computeOPT(i-1, inNY)

    # memoize (storage part)
    OPT[(i, inNY)] = cityCost + min(move, noMove)

    # return to caller
    return OPT[(i, inNY)]

print min(computeOPT(n, True), computeOPT(n, False))


OPT = {}
def iterativeComputeOPT():
    """iterative version"""
    OPT[(0, True)] = 0
    OPT[(0, False)] = 0
    for i in range(1, n+1):
        for bool in [True, False]:
            curCost = N[i] if bool else S[i]
            move = OPT[(i-1, not bool)] + M
            noMove = OPT[(i-1, bool)]
            OPT[(i, bool)] = curCost + min(move, noMove)

    print min(OPT[(n, True)], OPT[(n, False)])

iterativeComputeOPT()
