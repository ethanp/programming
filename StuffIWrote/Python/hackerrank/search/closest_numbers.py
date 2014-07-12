'''
7/12/14, 2:31 PM
Ethan Petuchowski
closest_numbers

https://www.hackerrank.com/challenges/closest-numbers
'''

input() # ignore
arr = sorted(map(int, raw_input().split()))
diffs = [arr[i+1]-arr[i] for i in range(len(arr)-1)]
min_diff = min(diffs)
coll = []
for i, diff in enumerate(diffs):
    if diff == min_diff:
        coll += [arr[i], arr[i+1]]
print ' '.join(map(str, coll))
