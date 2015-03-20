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

### TABLE 3
def allocation_of_functions_and_data_to_components(data, for_import=True):
    base = '%s' if for_import else '\t%s'
    for comp in data.components:
        print '\n%s\n------------' % comp.name
        print 'Functions'
        print '\n'.join(map(lambda x: base % x.name, comp.functions))
        print '\nData'
        print '\n'.join(map(lambda x: base % x.name, comp.IO_set()))

### TABLES 4 - 6
# Requires some work by-hand
# c.print_IO_dependencies()

### UML
# c.print_uml()

### METRICS
## Coupling and Cohesion
def coupling_and_cohesion(data):
    # IO btn components (metric 1)
    for comp in data.components:
        deps = set() # dependent components (metric 2)

        # inputs (across all functions) received from another component
        events_in = 0
        for inp in comp.inputs():
            for o in data.outputs():
                if inp == o and o.component() != comp:
                    events_in+=1
                    deps.add(o.component())

        # + outputs sent to another component
        events_out = 0
        for outp in comp.outputs():
            for i in data.inputs():
                if outp == i and i.component() != comp:
                    events_out+=1
                    deps.add(i.component())

        # Degree of cohesion
        deg = 0.
        # TODO

        # print table
        print 'Component,In,Out,Total,Deps'
        print '%s,%d,%d,%d,%d' % (
            comp.name,
            events_in, events_out,
            events_in + events_out,
            len(deps))




def main():
    c = read_db()
    coupling_and_cohesion(c)

if __name__ == '__main__':
    main()
