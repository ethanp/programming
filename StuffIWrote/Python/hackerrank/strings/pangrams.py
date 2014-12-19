'''
12/16/14, 9:51 PM
Ethan Petuchowski
pangrams.py

decide if a sentence contains all letters
'''
import string

s = raw_input()
d = set(string.lowercase)
for l in s.lower():
    if l in d:
        d.remove(l)
if len(d) == 0:
    print "pangram"
else:
    print "not pangram"
