'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py
'''

from util import *


def list_all(elems, names=False):
    for idx, elem in enumerate(elems):
        idx+=1
        print '%d: %s' % (idx, (elem.name if names else elem))

def main():
    c = read_db()
    f = functions(c)
    i = inputs(c)
    o = outputs(c)
    print_uml(c)


if __name__ == '__main__':
    main()
