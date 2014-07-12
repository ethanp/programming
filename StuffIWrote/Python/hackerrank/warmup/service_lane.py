'''
7/12/14, 12:00 PM
Ethan Petuchowski
service_lane

Given the entry and exit point of Calvin's vehicle
in the service lane, output the type of largest vehicle
which can pass through the service lane (including
the entry & exit segment)
'''


#### parse input

# N = num segments
# T = num test cases
N, T = map(int, raw_input().split(' '))
widths = map(int, raw_input().split(' '))


#### eval tests

for _ in range(T):

    # incLUsive
    i, j = map(int, raw_input().split(' '))

    print min(widths[i:j+1])
