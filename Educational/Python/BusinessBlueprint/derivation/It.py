'''
3/17/15, 9:40 PM
Ethan Petuchowski
It.py

Everything, and maybe move it later
'''

import os

COMP = 0
FUNC = 1

class Component(object):
    def __init__(self, name):
        self.name = name
        self.functions = []


class Function(object):
    def __init__(self, name):
        self.name = name
        self.inputs = []
        self.outputs = []

def parse_file():
    '''
    Returns:

        components = [
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
                comp = items[COMP]
                function = items[FUNC]

                if comp and not comp.isspace():
                    component = Component(comp)
                    components.append(component)

                if function and not function.isspace():
                    # remove initial number and space
                    function = function.split('.')[1][1:]
                    component.functions.append(Function(function))

    for c in components:
        print c.name
        print '----------------'
        for f in c.functions:
            print f.name
        print
    return components

def transform_to_dir(components):
    # create outer dir
    if not os.path.exists('components'):
        os.mkdir('components')
    for component in components:
        # create funcs dir
        funcs_dir_name = '/'.join([component.name, 'functions'])
        funcs_dir = os.path.dirname('functions')
        if not os.path.exists(funcs_dir):
            os.mkdir(funcs_dir)
        for function in component.functions:
            # create func dir
            func_dir_name = '/'.join([funcs_dir_name, function.name])
            func_dir = os.path.dirname('components')
            if not os.path.exists(func_dir):
                os.mkdir(func_dir)
            in_name = '/'.join([func_dir_name, 'input'])
            out_name = '/'.join([func_dir_name, 'output'])
            # create I/O files
            in_f = open(in_name, 'w+')
            out_f = open(out_name, 'w+')

def read_from_dir():
    '''
    :return: a list of Component's
    '''
    for component in os.listdir('components'):
        for func in os.listdir('/'.join(['components', component])):
            pass

def main():
    transform_to_dir(parse_file())


if __name__ == '__main__':
    main()
