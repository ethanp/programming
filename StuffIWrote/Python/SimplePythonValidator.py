# coding=utf-8

# Ethan Petuchowski
# March 16, 2014

_LINE = 0
_STRT = 0

def validate(input_text):
    '''
    return True iff the input string forms a valid python
    program according to a simplified set of rules:

        1. Lines ending in a colon must be followed by another line, whose indentation must be greater
        2. Lines not ending in a colon must not be followed by a line with greater indentation
        3. Lines receding indentation levels must match a previous indentation level
        4. Ignore comments, but comments can't start inside a *single-quoted* string

    BUG: this validator stumbles parsing the line that says
    `if line.count("'", 0, hash_loc) % 2 == 0:   # if it's not in a 'string'`
    It does not notice that the line's code ends with a ':' because it it thinks the
    comment is in a 'string' because there's only one "'" before it.  So sure, I could
    count the number of '"'s to its left, but that's turtles all the way down.

    SOLUTION: see remove_comment_from_line()


    BUG: closure scoping wasn't as simple as I was expecting

    SOLUTION 1: make it variable to modify inside closure an attribute of its declaring function
    stackoverflow.com/questions/3190706/nonlocal-keyword-in-python-2-x
    This is the best looking workaround, but it causes the PyCharm debugger to fail

    SOLUTION 2: wrap the variable into a list
    line_list = [line]
    line_list[0] = line_list[0][:close_loc[0]] etc.
    it's certainly gross but it's worth it for the debugger functionality

    SOLUTION 3: make it a global variable
    This is too hackish, though the soln I went with is just as hackish
    '''
    stack = [0]                             # default starting values
    follows_colon = False
    for line in input_text.splitlines():
        close_line = [line]

        def remove_comment_from_line():
            '''
            COMMENT finding ALGORITHM: starting on the left of the line, scan for the first of ('|"|#).
            If it's a #, then the comment stands. If it's a ('|"), we've opened up a string. Now
            ignore everything up to the repeating \1, and start over.
            '''
            close_strt = [0]
            while close_strt[_STRT] < len(close_line[_LINE]):
                dbl_quot_loc = close_line[_LINE].find('"', close_strt[_STRT])
                hash_loc = close_line[_LINE].find('#', close_strt[_STRT])
                sgl_quot_loc = close_line[_LINE].find("'", close_strt[_STRT])

                def scoot(c, loc):
                    end_dbl = close_line[_LINE].find(c, loc+1)
                    if end_dbl != -1:
                        close_strt[_STRT] = end_dbl+1

                if hash_loc == -1:                      # nothing to be done if no '#' exists
                    return

                if -1 < sgl_quot_loc < dbl_quot_loc:    # use the first type of quote found, ignore the other
                    dbl_quot_loc = -1
                elif -1 < dbl_quot_loc < sgl_quot_loc:
                    sgl_quot_loc = -1

                if -1 < dbl_quot_loc < hash_loc:        # 'scoot' past whatever quote type was found
                    scoot('"', dbl_quot_loc)
                elif -1 < sgl_quot_loc < hash_loc:
                    scoot("'", sgl_quot_loc)

                elif hash_loc >= 0:                     # chop comment off of line
                    close_line[_LINE] = close_line[_LINE][:hash_loc]
                else:
                    break

        remove_comment_from_line()
        spaces = len(close_line[_LINE]) - len(close_line[_LINE].lstrip())
        if spaces == len(close_line[_LINE]):  continue   # ignore blank/all-whitespace lines
        if not follows_colon:           # rule 2: only indent after a colon
            while stack[-1] > spaces:
                stack.pop()             # stack can't be empty because stack[0] is always ≤ spaces
            if stack[-1] < spaces:      # rule 3: receding indentations must match something
                return False
        elif spaces > stack[-1]:        # rule 1: new indent level after a colon
            stack.append(spaces)
        else:
            return False                # line didn't match any rules
        follows_colon = close_line[_LINE].rstrip()[-1] == ':'
    return True


#########
# Tests #
#########

print validate(
"""
this should:
    pass
"""
) is True

print validate(
"""
this should:
fail
"""
) is False

print validate(
"""
    this should:
fail
"""
) is False

print validate(
"""
this should:
    this should:
fail
"""
) is False

# Acknowledgments: M.C. Escher
print validate(
"""
this better:
    pass!

# coding=utf-8

# Ethan Petuchowski
# March 16, 2014

import re

def validate(input):
    '''
    return True iff the input string forms a valid python
    program according to a simplified set of rules:

        1. Lines ending in a colon must be followed by another line, whose indentation must be greater
        2. Lines not ending in a colon must not be followed by a line with greater indentation
        3. Lines receding indentation levels must match a previous indentation level
    '''
    stack = [0]                             # default starting values
    follows_colon = False

    for line in input.splitlines():

        search_start = 0
        for hash_num in range(line.count('#')):         # rule 4: ignore comments not in 'strings'
            hash_loc = line.find('#', search_start)     # find i'th #
            if line.count("'", 0, hash_loc) % 2 == 0:   # if it's not in a 'string'
                line = line[:hash_loc]                  # remove the rest of the line
            search_start = hash_loc + 1                 # iterate to next #

        spaces = len(line) - len(line.lstrip())

        if spaces == len(line):  continue   # ignore blank/all-whitespace lines

        if not follows_colon:               # rule 2: only indent after a colon
            while stack[-1] > spaces:
                stack.pop()
            if stack[-1] < spaces:          # rule 3: receding indentations must match something
                return False

        elif spaces > stack[-1]:            # rule 1: new indent level after a colon
            stack.append(spaces)

        else: return False

        follows_colon = line.rstrip()[-1] == ':'
    return True
"""
) is True

print validate(
"""
this should:
    this should:
        pass
# coding=utf-8

# Ethan Petuchowski
# March 16, 2014

_LINE = 0
_STRT = 0

def validate(input_text):
    stack = [0]                             # default starting values
    follows_colon = False
    for line in input_text.splitlines():
        close_line = [line]

        def remove_comment_from_line():
            close_strt = [0]
            while close_strt[_STRT] < len(close_line[_LINE]):
                dbl_quot_loc = close_line[_LINE].find('"', close_strt[_STRT])
                hash_loc = close_line[_LINE].find('#', close_strt[_STRT])
                sgl_quot_loc = close_line[_LINE].find("'", close_strt[_STRT])

                def scoot(c, loc):
                    end_dbl = close_line[_LINE].find(c, loc+1)
                    if end_dbl != -1:
                        close_strt[_STRT] = end_dbl+1

                if hash_loc == -1:                      # nothing to be done if no '#' exists
                    return False

                if -1 < sgl_quot_loc < dbl_quot_loc:    # use the first type of quote found, ignore the other
                    dbl_quot_loc = -1
                elif -1 < dbl_quot_loc < sgl_quot_loc:
                    sgl_quot_loc = -1

                if -1 < dbl_quot_loc < hash_loc:        # 'scoot' past whatever quote type was found
                    scoot('"', dbl_quot_loc)
                elif -1 < sgl_quot_loc < hash_loc:
                    scoot("'", sgl_quot_loc)

                elif hash_loc >= 0:                     # chop comment off of line
                    close_line[_LINE] = close_line[_LINE][:hash_loc]
                else:
                    break

        remove_comment_from_line()
        spaces = len(close_line[_LINE]) - len(close_line[_LINE].lstrip())
        if spaces == len(close_line[_LINE]):  continue   # ignore blank/all-whitespace lines
        if not follows_colon:           # rule 2: only indent after a colon
            while stack[-1] > spaces:
                stack.pop()             # stack can't be empty because stack[0] is always ≤ spaces
            if stack[-1] < spaces:      # rule 3: receding indentations must match something
                return False
        elif spaces > stack[-1]:        # rule 1: new indent level after a colon
            stack.append(spaces)
        else:
            return False                # line didn't match any rules
        follows_colon = close_line[_LINE].rstrip()[-1] == ':'
    return True

"""
) is True
