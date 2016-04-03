# 4/3/16
# Ethan
# case invariant replace

"""
python case_invariant_replace.py <original> <replacement> <file>

The CASE of the ORIGINAL STRING will be PRESERVED AFTER the
REPLACEMENT of the characters.

Original case-patterns must match one of the following

    abcd
    Abcd   # makes most of camel-casing use-cases work
    ABCD

"""

import os
import sys


def perform_replacement(text, orig, repl):
    while orig in text.lower():
        start_index = text.lower().find(orig)
        end_index = start_index + len(orig)
        raw_original = text[start_index:end_index]

        def transform_line(replacement_string):
            return text[:start_index] + replacement_string + text[end_index:]

        if raw_original.islower():
            text = transform_line(repl)
        elif raw_original.isupper():
            text = transform_line(repl.upper())
        elif raw_original.istitle():
            text = transform_line(repl.title())
        else:
            raise AssertionError(
                'unknown casing: %s' % raw_original)
    return text


# run script if arguments were given
if len(sys.argv) == 4:
    original = sys.argv[1].lower()
    replacement = sys.argv[2].lower()
    filename = sys.argv[3]
    tmp_filename = filename + '.tmp'
    with open(filename) as the_file, open(tmp_filename, 'w') as tmp_file:
        for line in the_file:
            tmp_file.write(line + '\n')

    os.remove(filename)
    os.rename(tmp_filename, filename)
    sys.exit(0)


def test_original_text_is_not_modified():
    test_text = 'asdf'
    done = perform_replacement(test_text, 's', 'd')
    assert test_text == 'asdf'
    assert done == 'addf'
    print 'passed 1'


def test_works_on_lower():
    test_text = 'asdf_qwer'
    done = perform_replacement(test_text, 'qwe', 'zxcvb')
    assert done == 'asdf_zxcvbr'
    print 'passed 2'


def test_works_on_title():
    test_text = 'AsdfQwer'
    done = perform_replacement(test_text, 'qwe', 'zxcvb')
    assert done == 'AsdfZxcvbr'
    print 'passed 3'


def test_works_on_upper():
    test_text = 'ASDFQWER'
    done = perform_replacement(test_text, 'qwe', 'zxcvb')
    assert done == 'ASDFZXCVBR'
    print 'passed 4'


# run tests if no arguments were given
if len(sys.argv) == 1:
    test_original_text_is_not_modified()
    test_works_on_lower()
    test_works_on_title()
    test_works_on_upper()
    sys.exit(0)
