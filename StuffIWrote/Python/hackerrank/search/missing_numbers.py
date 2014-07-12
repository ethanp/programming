'''
7/12/14, 2:43 PM
Ethan Petuchowski
missing_numbers

https://www.hackerrank.com/challenges/missing-numbers
'''
from collections import Counter
def get(): return Counter(raw_input().split())
input()
A = get()
input()
B = get()
diff = sorted((B - A).keys())
print ' '.join(diff)
