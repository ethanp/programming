'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py
'''

from util import *


def list_all(elems):
    for idx, elem in enumerate(elems):
        idx+=1
        print '%d: %s' % (idx, str(elem))

###############
### TABLE 3 ###
###############
def allocation_of_functions_and_data_to_components(data):
    for comp in data.components:
        print '\n%s\n------------' % comp.name
        print 'Functions'
        print '\n'.join(map(lambda x: '\t%s' % x.name, comp.functions))
        print '\nData'
        print '\n'.join(map(lambda x: '\t%s' % x.name, comp.IO()))


def main():
    c = read_db()
    # list_all(sorted(map(str, c.outputs())))
    # print c
    allocation_of_functions_and_data_to_components(c)

if __name__ == '__main__':
    main()
