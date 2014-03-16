'''
Created on Dec 24, 2012
stackoverflow.com/questions/231767/the-python-yield-keyword-explained
'''

# Note that this is a Method, Not a Class
def createGenerator():
    mylist = range(3)
    for i in mylist :
        yield i*i
        
mygenerator = createGenerator()
print(mygenerator)

    
for j in mygenerator:
    print(j)

# The generator has already been run-through once,
#    so it no longer works
for i in mygenerator:
    print(i)

'''
Python in a nutshell Generators
'''

def updown(N):
    '''counts up from 1 to N, then back to 1'''
    for x in xrange(1, N): yield x
    for x in xrange(N, 0, -1): yield x
    return

print '\n', updown(3), '\n'
for i in updown(3): print i,

print
print

print sum(x * x for x in range(10))

"""
    The following code doesn't work because I can't figure out
    how to Get it to work!
"""
g = updown(3)
i = g.next()
while i:
    print i,
    i = g.next()
