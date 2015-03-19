'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py
'''

from util import *


def list_all(elems):
    ''' e.g. list_all(sorted(map(str, c.outputs()))) '''
    for idx, elem in enumerate(elems):
        idx+=1
        print '%d: %s' % (idx, str(elem))

###############
### TABLE 3 ###
###############
def allocation_of_functions_and_data_to_components(data, for_import=True):
    base = '%s' if for_import else '\t%s'
    for comp in data.components:
        print '\n%s\n------------' % comp.name
        print 'Functions'
        print '\n'.join(map(lambda x: base % x.name, comp.functions))
        print '\nData'
        print '\n'.join(map(lambda x: base % x.name, comp.IO_set()))

#########################################
###         TABLES 4 - 6              ###
#   Requires a small amount of effort   #
#           to be done by hand          #
#########################################
# c.print_IO_dependencies()

#############################
### UML DIAGRAM FUNCTIONS ###
#############################
# c.print_uml()

def main():
    c = read_db()
    c.print_uml()

if __name__ == '__main__':
    main()
