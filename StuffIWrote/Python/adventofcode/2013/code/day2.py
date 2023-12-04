import re

HERE = '/Users/Ethan/code/my-code/ProgrammingGit/StuffIWrote/Python/adventofcode/2013/inputs/'
SAMPLE = HERE + 'sampleInput2.txt'
REAL = HERE + 'input2.txt'

def solve1():
    allowable = {'red' : 12, 'green': 13, 'blue': 14}
    acc = 0
    with open(REAL, 'r') as f:
        for game in f:
            gameId = check1(game, allowable)
            if gameId > 0:
                acc += gameId
    print(acc)

def check1(game, allowable):
    colonIdx = game.find(':')
    gameId = int(game[5:colonIdx])
    sets = game[colonIdx+2:].strip().split(';')
    for s in sets:
        dice = s.split(',')
        for die in dice:
            elems = die.strip().split(' ')
            num, name = elems[0], elems[1]
            if allowable[name] < int(num):
                return -1
    return gameId

def solve2():
    acc = 0
    with open(REAL, 'r') as f:
        for game in f:
            allowable = check2(game)
            power = 1
            for v in allowable.values():
                power *= v
            acc += power
    print(acc)

def check2(game):
    allowable = {'red' : 0, 'green': 0, 'blue': 0}
    colonIdx = game.find(':')
    gameId = int(game[5:colonIdx])
    sets = game[colonIdx+2:].strip().split(';')
    for s in sets:
        dice = s.split(',')
        for die in dice:
            elems = die.strip().split(' ')
            num, name = int(elems[0]), elems[1]
            if allowable[name] < num:
                allowable[name] = num
    return allowable

if __name__ == "__main__":
    solve2()