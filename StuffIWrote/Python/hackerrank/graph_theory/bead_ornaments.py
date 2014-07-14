'''
7/13/14, 4:24 PM
Ethan Petuchowski
bead_ornaments

https://www.hackerrank.com/challenges/beadornaments
'''

"""
this seems to be on the right track but it's not there yet
because I haven't considered the case of stringing the beads
together as a tree.
"""

def factorial(n):
    if n == 1: return 1
    c = 1
    for i in range(2, n+1):
        c *= i
    return c


def main():
    # read input
    num_cases = input()
    for i in range(num_cases):
        num_colors = input()
        color_counts = map(int, raw_input().split())
        in_color_perms = 1
        for j in color_counts:
            in_color_perms *= factorial(j)
        btn_color_perms = factorial(num_colors)
        print (in_color_perms * btn_color_perms / 2) % 1000000007

if __name__ == '__main__':
    main()
