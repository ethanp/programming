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
        print '\n'.join(map(lambda x: '\t%s' % x.name, comp.IO_set()))

#########################################
###         TABLES 4 - 6              ###
#   This produces a starting point      #
# the rest will have to be done by hand #
#########################################

class Conflict(object):
    def __init__(self, in_func, out_func, out_comp, in_comp, io_name):
        self.in_func = in_func
        self.out_func = out_func
        self.out_comp = out_comp
        self.in_comp = in_comp
        self.io_name = io_name

    def __str__(self):
        return '%s,%s,%s,%s,%s' % (
            self.out_comp, # from comp
            self.in_comp,  # to comp
            self.in_func,  # this func requires
            self.out_func, # from that func
            self.io_name   # the input arg
        )

def con_no_con(data):
    ''' if something DOESN'T conflict, then I guess it's "External"? '''
    no_con,con=[],[]
    for comp in data.components:
        for func in comp.functions:
            for i in func.inputs:
                for o in data.outputs():
                    c = Conflict(
                            func.name, o.function.name,     # functions
                            o.component().name, comp.name,  # components
                            i.name)                         # parameter

                    if i == o:
                        no_con.append(c)
                    else:
                        con.append(c)
    return no_con,con


def io_dependencies(data):
    no_conflicts, conflicts = con_no_con(data)

    print 'Dependencies between components:'
    print '\n'.join(str(c) for c in conflicts if c.out_comp != c.in_comp)
    print '\nDependencies within components:'
    print '\n'.join(str(c) for c in conflicts if c.out_comp == c.in_comp)
    print '\nExternal dependencies:'
    print '\n'.join(str(c) for c in no_conflicts)


def main():
    c = read_db()
    # list_all(sorted(map(str, c.outputs())))
    # print c
    # allocation_of_functions_and_data_to_components(c)
    io_dependencies(c)

if __name__ == '__main__':
    main()
