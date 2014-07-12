'''
7/12/14, 12:50 PM
Ethan Petuchowski
lonely_integer

https://www.hackerrank.com/challenges/lonely-integer
'''
def lonelyinteger(a):
    s = set()
    for i in a:
        if i in s:
            s.remove(i)
        else:
            s.add(i)
    return list(s)[0]

if __name__ == '__main__':
    a = input()
    b = map(int, raw_input().strip().split(' '))
    print lonelyinteger(b)

