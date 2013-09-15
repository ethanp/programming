'''
Created on Dec 24, 2012

@author: Ethan
'''

def makeInc(x):
    def inc(y):
        # x is "closed" in the definition of inc
        return y + x
    return inc

inc5 = makeInc(5)
inc10 = makeInc(10)

print inc5 (5) # returns 10
print inc10(5) # returns 15

