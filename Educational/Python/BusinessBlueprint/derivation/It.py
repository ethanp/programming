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
            # create I/O files
            in_f = open('input.txt', 'w+')
            out_f = open('output.txt', 'w+')

            os.chdir('..')

        os.chdir('..')

def read_from_dir():
    '''
    :return: a list of Component's
    '''
    os.chdir('components')
    for component in os.listdir('.'):
        os.chdir(component)
        for func in os.listdir('.'):
            os.chdir(func)

            pass

def main():
    transform_to_dir(parse_file())
    # read_from_dir()


if __name__ == '__main__':
    main()
