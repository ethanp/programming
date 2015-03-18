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
    os.chdir('components')
    for component in components:
        # create funcs dir
        if not os.path.exists('functions'):
            os.mkdir('functions')
        os.chdir('functions')
        for function in component.functions:
            # create func dir
            if not os.path.exists(function.name):
                os.mkdir(function.name)
            os.chdir(function.name)
            # create I/O files
            in_f = open('input.txt', 'w+')
            out_f = open('output.txt', 'w+')
            os.chdir('..')
        os.chdir('..')

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
