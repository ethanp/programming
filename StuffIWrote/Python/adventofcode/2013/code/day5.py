DAY_NUM = 5
REPO_LOC = '/Users/Ethan/code/my-code/ProgrammingGit'
BASE = REPO_LOC + '/StuffIWrote/Python/adventofcode/2013/inputs'
TEMPLATE = BASE + '/%s/input%d.txt' % ('%s', DAY_NUM)
SAMPLE = TEMPLATE % 'sample'
REAL = TEMPLATE % 'actual'


def asInts(strings):
    return [int(elem) for elem in strings if elem]


class Mapper(object):

    def __init__(self, lines):
        self.internal = []
        for line in lines:
            self.internal.append(asInts(line.split(' ')))

    def mapSource(self, source):
        for m in self.internal:
            if m[1] <= source < m[1] + m[2]:
                return m[0] + (source - m[1])
        return source


NUM_MAPS = 7
maps = []
cache = {}


def parseMaps(lines):
    global maps
    for mapIdx in range(NUM_MAPS):
        acc = []
        while True:
            if not lines:
                maps.append(Mapper(acc))
                break
            line = lines.pop(0).strip()
            if line:
                acc.append(line)
            else:
                maps.append(Mapper(acc))
                lines.pop(0)
                break


def location(seed):
    if seed in cache:
        return cache[seed]
    curr = seed
    for m in maps:
        curr = m.mapSource(curr)
    cache[seed] = curr
    return curr


def solve1():
    with open(REAL, 'r') as f:
        lines = f.readlines()
        seeds = asInts(lines[0].split(':')[1].split(' '))
        parseMaps(lines[3:])
        print('result: %d' % min(map(location, seeds)))


def solve2():
    """ Optimisation? CPU go brrrrr """
    with open(REAL, 'r') as f:
        lines = f.readlines()
        seedsLine = asInts(lines[0].split(':')[1].split(' '))
        groupSize = 2
        seedsPairs = [seedsLine[k:k + groupSize] for k in range(0, len(seedsLine), groupSize)]
        parseMaps(lines[3:])
        print('finding min now')
        minim = -1
        for start, length in seedsPairs:
            print('start, length = (%d, %d)' % (start, length))
            r = range(start, start + length)
            for seed in r:
                loc = location(seed)
                if minim < 0 or loc < minim:
                    print('loc %d' % loc)
                    minim = loc
        print('result: %d' % minim)


if __name__ == "__main__":
    solve2()
