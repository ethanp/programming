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
    print 'Component,In,Out,Total,' \
          'Deps,NumFuncs,' \
          'Num Funcs Send Within,% Funcs Send Within'

    for comp in data.components:

        deps = set() # dependent components (metric 2)
        degree_of_cohesion = 0
        events_out = 0
        events_in = 0

        for func in comp.functions:

            # inputs (across all functions) received from another component

            p = False
            for inp in func.inputs:
                for o in data.outputs():
                    if inp == o and o.component() != comp:
                        p = True
                        events_in+=1
                        deps.add(o.component())

            # + outputs sent to another component
            for outp in func.outputs:
                for i in data.inputs():
                    if outp == i and i.component() != comp:
                        p = True
                        events_out+=1
                        deps.add(i.component())

            if not p:
                degree_of_cohesion += 1

        # Degree of cohesion
        perc_cohesion = '%d%%' % int(
            float(degree_of_cohesion) / len(comp.functions)*100)

        print '%s,%d,%d,%d,%d,%d,%d,%s' % (
            comp.name, events_in, events_out, events_in + events_out,
            len(deps), len(comp.functions),
            degree_of_cohesion, perc_cohesion
        )

def total_io(data):
    for comp in data.components:
        print '%s: %d' % (comp.name, len(set(comp.inputs() + comp.outputs())))


def main():
    c = read_db()
    # coupling_and_cohesion(c)
    total_io(c)

if __name__ == '__main__':
    main()
