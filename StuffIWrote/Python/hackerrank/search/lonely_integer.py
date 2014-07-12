'''
7/12/14, 12:50 PM
Ethan Petuchowski
lonely_integer

https://www.hackerrank.com/challenges/lonely-integer
'''
input()
a = map(int, raw_input().strip().split(' '))
s = set()
for i in a:
    if i in s:
        s.remove(i)
    else:
        s.add(i)
print list(s)[0]
