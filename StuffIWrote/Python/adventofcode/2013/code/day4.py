import re
from collections import defaultdict

DAY_NUM = 4
HERE = '/Users/Ethan/code/my-code/ProgrammingGit/StuffIWrote/Python/adventofcode/2013/inputs/'
SAMPLE = HERE + 'sampleInput%d.txt' % DAY_NUM
REAL = HERE + 'input%d.txt' % DAY_NUM

def solve1():
    acc = 0
    with open(REAL, 'r') as f:
        for line in f.readlines():
            a, b = line.split(':')[1].split('|')
            winning = {int(s) for s in a.split(' ') if s}
            have = {int(s) for s in b.split(' ') if s}
            inter = winning.intersection(have)
            print('overlap ' + str(inter))
            if inter:
                acc += 2 ** (len(inter) - 1)
    print(acc)
    
def solve2():
    acc = 0
    with open(REAL, 'r') as f:
        extras = defaultdict(int)
        for idx, line in enumerate(f.readlines()):
            acc += 1
            a, b = line.split(':')[1].split('|')
            winning = {int(s) for s in a.split(' ') if s}
            have = {int(s) for s in b.split(' ') if s}
            inter = winning.intersection(have)
            wins = len(inter)
            r = range(idx+2, idx+2+wins)
            for i in r:
                extras[i] += 1 + extras[idx+1]
            print(idx, wins, str(extras))
        for i in extras.values():
            acc += i
            
    print(acc)
    
if __name__ == "__main__":
    solve2()
