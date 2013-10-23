## 10/22/13
## Algorithm Design, problem 6.9(b)

x = [10, 1, 7, 7]
s = [ 8, 4, 2, 1]

prev_results = {}
def compute_OPT(x, s, i):
    if i in prev_results.keys():
        return prev_results[i]
    the_max = 0
    for j in range(i):
        this_sum = 0
        for k in range(i-j):
            this_sum += min(x[j+k], s[k])
        this_sum += compute_OPT(x, s, j-1)
        if this_sum > the_max:
            the_max = this_sum
    prev_results[i] = the_max
    return the_max

print compute_OPT(x, s, 4)