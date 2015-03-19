'''
3/17/15, 11:35 PM
Ethan Petuchowski
util.py
'''
import os

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
            self.in_func,  # this func [wants]
            self.io_name,  # the input arg required
            self.out_func # from that func
        )

    @staticmethod
    def header():
        return 'FROM,TO,FUNCTION,REQUIRES,FROM FUNC'

    def __lt__(self, other):
        return self.out_comp < other.out_comp   \
            if self.out_comp != other.out_comp  \
            else self.in_comp < other.in_comp   \
            if self.in_comp != other.in_comp    \
            else self.io_name < other.io_name

class Components(object):
    def __init__(self):
        self.components = []

    def print_components(self):
        print ''.join(map(str, self.components))

    def print_uml(self):
        print ''.join((cp.uml_str() for cp in self.components))

    def functions(self):
        return [f for c in self.components for f in c.functions]

    def inputs(self):
        return [i for f in self.functions() for i in f.inputs]

    def outputs(self):
        return [o for f in self.functions() for o in f.outputs]

    def add(self, component):
        self.components.append(component)

    def __str__(self):
        return ''.join(map(str, self.components))

    def con_no_con(self):
        ''' if something DOESN'T conflict, then I guess it's "External"? '''
        no_con, con = [], []
        for comp in self.components:
            for func in comp.functions:
                for i in func.inputs:
                    p = False
                    for o in self.outputs():
                        if i == o:
                            p = True
                            c = Conflict(
                                func.name, o.function.name,  # functions
                                o.component().name, comp.name,  # components
                                i.name)  # parameter
                            con.append(c)
                    if not p:
                        no_con.append(Conflict(
                            func.name, 'external', 'external', comp.name,
                            i.name))

        return sorted(no_con), sorted(con)

    def print_IO_dependencies(self):
        no_conflicts, conflicts = self.con_no_con()
        print 'Dependencies between components:'
        print Conflict.header()
        print '\n'.join(str(c) for c in conflicts if c.out_comp != c.in_comp)
        print '\nDependencies within components:'
        print Conflict.header()
        print '\n'.join(str(c) for c in conflicts if c.out_comp == c.in_comp)
        print '\nExternal dependencies:'
        print Conflict.header()
        print '\n'.join(str(c) for c in no_conflicts)


class Component(object):
    def __init__(self, name):
        self.name = name
        self.functions = []

    def string_header(self, param):
        return '%s\n-----------------\n%s\n\n' % (
            self.name, '\n'.join(param))

    def __str__(self):
        return self.string_header(map(str, self.functions))

    def uml_str(self):
        return self.string_header(map(lambda x: x.uml_str(), self.functions))

    def add(self, function):
        os.chdir(function)
        fcn = Function(function, self)
        fcn.set_IO_from_file()
        self.functions.append(fcn)
        os.chdir('..')

    def IO_set(self):
        return sorted(set(self.inputs() + self.outputs()))

    def inputs(self):
        return [i for f in self.functions for i in f.inputs]

    def outputs(self):
        return [o for f in self.functions for o in f.outputs]


class Function(object):
    def __init__(self, name, component):
        self.name = name
        self.component = component
        self.inputs = []
        self.outputs = []

    def io_str(self, ioz, name):
        return '\t%s:\n%s' % (name, ''.join(map(
            lambda x: '\t\t%s' % x, ioz
        ))) if ioz else '\tNo %s\n' % name

    def inputs_string(self):
        return self.io_str(self.inputs, 'Inputs')

    def outputs_string(self):
        return self.io_str(self.outputs, 'Outputs')

    def __str__(self):
        return '%s\n%s%s' % (
            self.name,
            self.inputs_string(),
            self.outputs_string()
        )

    def set_IO_from_file(self):
        ''' requires that pwd be the exact place where these files are '''
        self.inputs = self.read_IO_from_file('input')
        self.outputs = self.read_IO_from_file('output')

    def read_IO_from_file(self, to_get):
        ''' requires that pwd be the exact place where these files are '''
        return [IorO(line.strip(), self)
                for line in open(to_get + '.txt')
                if line and not line.isspace()]

    def uml_str(self):
        return '+ %s()' % self.name

class IorO(object):
    def __init__(self, name, function):
        self.name = name
        self.function = function

    def component(self):
        return self.function.component

    def __str__(self):
        return self.name

    def __lt__(self, other):
        return self.name < other.name

    def __eq__(self, other):
        return self.name == other.name

    def __hash__(self):
        return hash(self.name)

def read_db():
    ''' read the dir structure into a Components object (returned) '''
    components = Components()
    os.chdir('components')
    for comp_name in os.listdir('.'):
        if comp_name == '.DS_Store': continue
        os.chdir(comp_name)
        component = Component(comp_name)
        components.add(component)
        for func_name in os.listdir('.'):
            if func_name == '.DS_Store': continue
            component.add(func_name)
        os.chdir('..')
    return components
