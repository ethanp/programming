from termcolor import colored
import subprocess

for ls_output in subprocess.check_output(["ls", "-l"]).split('\n'):
    outstr = ls_output[ls_output.find(' '):]
    def prepend(s):
        global outstr
        outstr = s+outstr
    first_word_backwards = ls_output.split(' ')[0][::-1]
    if not first_word_backwards \
        or first_word_backwards[-1] not in ('d', '-'):
        print ls_output
        continue
    for char in first_word_backwards:
        if char == 'd': prepend(colored('d', 'green'))
        if char == 'r': prepend(colored('r', 'red'))
        if char == 'x': prepend(colored('x', 'blue'))
        if char == 'w': prepend(colored('w', 'cyan'))
        if char == '@': prepend(colored('@', 'magenta'))
        if char == '-': prepend(colored('-', 'white'))
    print outstr
