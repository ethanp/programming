from pathlib import Path


def asInts(strings):
    return [int(elem) for elem in strings if elem]


def parseInputLine1(line):
    return asInts(line.split(':')[1].split(' '))


def parseInputLine2(line):
    return int(line.split(':')[1].replace(' ', '').strip())


def solve1(lines):
    times = parseInputLine1(lines[0])
    distances = parseInputLine1(lines[1])
    acc = 1
    for time, distance in zip(times, distances):
        numWinners = 0
        for t in range(1, time):
            dist = t * (time - t)
            if dist > distance:
                numWinners += 1
        acc *= numWinners
    print('Final result: %d' % acc)


def solve2(lines):
    time = parseInputLine2(lines[0])
    distance = parseInputLine2(lines[1])
    acc = 0
    print('t: %d, d: %d' % (time, distance))
    for t in range(1, time):
        dist = t * (time - t)
        if dist > distance:
            acc += 1
    print('Final result: %d' % acc)


SAMPLE = 'sample'
ACTUAL = 'actual'

whichInput = ACTUAL

if __name__ == "__main__":
    dayNum = __file__.split('/')[-1].split('.')[0][3:]
    filename = 'input%s.txt' % dayNum
    inputFile = Path(__file__).parents[1] / 'inputs' / whichInput / filename
    with open(inputFile, 'r') as f:
        solve2(f.readlines())
