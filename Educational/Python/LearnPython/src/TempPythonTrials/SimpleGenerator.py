'''
Created on Dec 24, 2012
http://stackoverflow.com/questions/231767/
        the-python-yield-keyword-explained/231855#231855
@author: Ethan
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