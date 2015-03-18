'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py

Everything, and maybe move it later
'''

import os

PRINT = False

class Component(object):
    def __init__(self, name):
        self.name = name
        self.functions = []

class Function(object):
    def __init__(self, name):
        self.name = name
        self.inputs = []
        self.outputs = []

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

def parse_file():
    '''
    Returns: components = [
        'component1' : {
            'function1' : {
                'inputs' : [input1, input2, ...],
                'outputs' : [output1, output2, ...]
            },
            'function2' : {...},
            ...
        },
        'component2' : {...},
        ...
    ]

    '''
    components = []
    component = None
    with open('/Users/Ethan/Desktop/Components.csv') as f:
        for everything in f:
            for line in everything.split('\r'): # wtf?
                items = line.split(',') # csv
                if len(items) == 1: continue
                comp = items[0]
                function = items[1]
                if comp and not comp.isspace():
                    component = Component(comp)
                    components.append(component)
                if function and not function.isspace():
                    # remove initial number and space
                    function = function.split('.')[1][1:]
                    component.functions.append(Function(function))

    print_components(components)
    return components

def create_and_enter(dir_name):
    if not os.path.exists(dir_name):
        os.mkdir(dir_name)
    os.chdir(dir_name)

def transform_to_dir(components):
    create_and_enter('components')
    for component in components:
        create_and_enter(component.name)
        for function in component.functions:
            create_and_enter(function.name)
            open('input.txt', 'w+')     # create I/O files
            open('output.txt', 'w+')
            os.chdir('..')
        os.chdir('..')

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
    print_components(components)



def main():
    # parse_file()
    read_from_dir()


if __name__ == '__main__':
    main()
