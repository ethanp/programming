'''
7/12/14, 12:55 PM
Ethan Petuchowski
encryption

https://www.hackerrank.com/challenges/encryption
'''

import sys
from math import *

word = raw_input()
height = int(ceil(sqrt(len(word))))
width = int(ceil(sqrt(len(word))))

square = []
for i in range(height):
    square.append(word[i*width:min(len(word),i*width+width)])
for i in range(width):
    for j in range(height):
        if i < len(square[j]):
            sys.stdout.write(square[j][i])
    sys.stdout.write(' ')
