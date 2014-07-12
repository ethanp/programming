'''
7/12/14, 12:26 PM
Ethan Petuchowski
gem_stones

A description in here
'''

N = input()
seen_on_all = set()
b = False
for _ in range(N):
    l_set = set()
    for c in raw_input():
        l_set.add(c)
    if not b:
        seen_on_all = l_set
        b = True
    seen_on_all.intersection_update(l_set)

print len(seen_on_all)
