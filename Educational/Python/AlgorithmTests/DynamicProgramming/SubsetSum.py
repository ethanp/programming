## 10/22/13
## Algorithms Test 2 Review Problem


S = [14, 69, 32, 1, 42, 53, 235, 63, 23, 6263, 32]
W = 15


# finding the set is O(nW)
table = {}
def OPT(i, w):
    if (i,w) in table:
        return table[(i,w)]
    if i < 0: return 0
    if S[i] > w:
        return OPT(i-1, w)
    first = OPT(i-1, w)
    second = S[i] + OPT(i - 1, w - S[i])
    table[(i,w)] = max(first,second)
    return table[(i,w)]


# recovering the set is O(n)
soln = []
def SOLN(i, w):
    if OPT(i,w) != w:
        return False
    if i < 0:
        return True
    if S[i] > w:
        return SOLN(i-1, w)
    first = OPT(i-1, w)
    second = S[i] + OPT(i-1, w-S[i])
    if first > second:
        return SOLN(i-1, w)
    else:
        soln.append(S[i])
        return SOLN(i-1,w-S[i])

if SOLN(len(S)-1,W):
    print soln
else:
    print "it's impossible!"
