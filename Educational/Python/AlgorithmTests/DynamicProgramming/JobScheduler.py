## 10/22/13
## Algorithm Design, problem 6.10(b)

A = [10, 1,  1, 10]
B = [ 5, 1, 20, 20]

n = len(A) - 1

def compute_opt(i, L, d={}):

    # base case
    if i < 0:
        return 0

    # figure out which list we're using
    if L == A:
        the_list = 'A'
        notL = B
    else:
        the_list = 'B'
        notL = A

    # unhashable type: 'list', workaround
    # still able to memoize
    if (i, the_list) in d.keys():
        return d[(i, the_list)]

    # recurrence relation
    opt = max(
        compute_opt(i - 1, L, d),
        compute_opt(i - 2, notL, d)
    ) + L[i]

    # store-step of memoization
    d[(i,the_list)] = opt
    return opt

def print_opt():
    print max(
        compute_opt(n, A),
        compute_opt(n, B)
    )

print_opt()
