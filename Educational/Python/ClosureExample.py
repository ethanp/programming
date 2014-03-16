##################################################
# Simple example of the power: December 24, 2012 #
##################################################

def makeInc(x):
    def inc(y):
        # x is "closed" in the definition of inc
        return y + x
    return inc

inc5 = makeInc(5)
inc10 = makeInc(10)

inc5 (5) # returns 10
inc10(5) # returns 15


#########################################################
# Solving problems with closure scoping: March 16, 2014 #
#########################################################

def outer():
    for x in ['stuff in here', 'and another', "there's more too"]:
        def inner():
            # x += 'zz'  # UnboundLocalError("local var 'x' ref'd before assnmt")
            print x
        inner()

outer()

def outer2():
    '''
    solution 1: wrap it in a list
    stackoverflow.com/questions/4851463/python-closure-write-to-variable-in-parent-scope
    '''
    for x in ['stuff in here', 'and another', "there's more too"]:
        y = [x]
        def inner():
            y[0] += 'zz'
            print y[0]
        inner()
outer2()


def outer3():
    '''
    solution 2: make it an attribute of the function
    stackoverflow.com/questions/3190706/nonlocal-keyword-in-python-2-x/3190786
    I think this one is easiest to read, but PyCharm is complaining about it
    '''
    for outer3.x in ['stuff in here', 'and another', "there's more too"]:
        def inner():
            outer3.x += '33'
            print outer3.x
        inner()
outer3()
