'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py
'''

from util import *


def list_all(elems, names=False):
    for idx, elem in enumerate(elems):
        idx+=1
        print '%d: %s' % (idx, str(elem).strip())

###############
### TABLE 3 ###
###############
def allocation_of_functions_and_data_to_components(components):
    IandO = components.inputs() + components.outputs()
    pass


def main():
    c = read_db()
    # print ''.join(sorted(map(str,c.inputs())))
    # print ''.join(sorted(map(str,c.outputs())))
    list_all(sorted(map(str, c.outputs())))
    # print c

if __name__ == '__main__':
    main()
