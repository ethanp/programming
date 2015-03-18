'''
3/17/15, 11:35 PM
Ethan Petuchowski
util.py
'''
import os

def print_components(components):
    print ''.join(map(repr, components))

def print_uml(components):
    print ''.join((cp.uml_str() for cp in components))

def functions(components):
    return [f for c in components for f in c.functions]

def inputs(components):
    return [i for f in functions(components) for i in f.inputs]

def outputs(components):
    return [o for f in functions(components) for o in f.outputs]

class Component(object):
    def __init__(self, name):
        self.name = name
        self.functions = []

    def string_header(self, param):
        return '%s\n-----------------\n%s\n\n' % (
            self.name, '\n'.join(param))

    def __repr__(self):
        return self.string_header(map(repr, self.functions))

    def uml_str(self):
        return self.string_header(map(lambda x: x.uml_str(), self.functions))

class Function(object):
    def __init__(self, name, component):
        self.name = name
        self.component = component
        self.inputs = []
        self.outputs = []

    def io_str(self, ioz, name):
        return '\t%s:\n%s' % (name, map(
            lambda x: '\t\t%s\n' % x, ioz
        )) if ioz else '\tNo %s' % name

    def inputs_string(self):
        return self.io_str(self.inputs, 'Inputs')

    def outputs_string(self):
        return self.io_str(self.outputs, 'Outputs')

    def __repr__(self):
        return '%s\n%s\n%s' % (
            self.name, self.inputs_string(), self.outputs_string()
        )

    def set_IO_from_file(self):
        self.inputs = self.read_IO_from_file('input')
        self.outputs = self.read_IO_from_file('output')

    def read_IO_from_file(self, to_get):
        return map(lambda x: IorO(x, self),
                   [line for line in open(to_get + '.txt')
                    if line and not line.isspace()])

    def uml_str(self):
        return '+ %s()' % self.name

class IorO(object):
    def __init__(self, name, function):
        self.name = name
        self.function = function

    def __repr__(self):
        return self.name

def read_db():
    '''
    :return: a list of Component's
    '''
    components = []
    os.chdir('components')
    comps = os.listdir('.')
    for comp_name in comps:
        os.chdir(comp_name)
        component = Component(comp_name)
        components.append(component)
        funcs = os.listdir('.')
        for func_name in funcs:
            os.chdir(func_name)
            function = Function(func_name, component)
            component.functions.append(function)
            function.set_IO_from_file()
            os.chdir('..')
        os.chdir('..')
    return components
