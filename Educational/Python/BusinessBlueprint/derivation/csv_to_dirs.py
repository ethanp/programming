'''
3/17/15, 11:37 PM
Ethan Petuchowski
csv_to_dirs.py
'''
import os
from derivation.util import print_components, Function, Component

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

def main():
    pass # already did its use, now it will just overwrite everything

if __name__ == '__main__':
    main()
