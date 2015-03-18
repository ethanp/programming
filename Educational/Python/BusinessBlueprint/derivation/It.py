'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py
'''

from util import print_components, Component, Function, read_from_dir

def print_inputs(comps):
    print 'listing inputs:'
    i = 0
    for comp in comps:
        for func in comp.functions:
            for inp in func.inputs:
                print '%s: %s' % (i, inp)
                i+=1

def main():
    # parse_file()
    components = read_from_dir()
    print_inputs(components)

if __name__ == '__main__':
    main()
