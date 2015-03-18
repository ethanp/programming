'''
3/17/15, 11:35 PM
Ethan Petuchowski
util.py
'''
import os


def print_io(ioz, name):
    if ioz:
        print name
        for io in ioz:
            print io
    else:
        print '\tno '+name

def print_func(func):
    print func.name
    print_io(func.inputs, 'inputs')
    print_io(func.outputs, 'outputs')

def print_component(comp):
    print comp.name + '\n----------------'
    for func in comp.functions:
        print_func(func)
    print

def print_components(components):
    for comp in components:
        print_component(comp)

class Component(object):
    def __init__(self, name):
        self.name = name
        self.functions = []

class Function(object):
    def __init__(self, name):
        self.name = name
        self.inputs = []
        self.outputs = []

def get_IO(IorO):
    return [line for line in open(IorO+'.txt')
            if line and not line.isspace()]

def read_from_dir():
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
            function = Function(func_name)
            component.functions.append(function)
            function.inputs = get_IO('input')
            function.outputs = get_IO('output')
            os.chdir('..')
        os.chdir('..')
    return components
