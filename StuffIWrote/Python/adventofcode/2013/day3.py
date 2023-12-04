import re

DAY_NUM = 3
HERE = '/Users/Ethan/code/my-code/ProgrammingGit/StuffIWrote/Python/adventofcode/2013/inputs/'
SAMPLE = HERE + 'sampleInput%d.txt' % DAY_NUM
REAL = HERE + 'input%d.txt' % DAY_NUM

def solve1():
    acc = 0
    with open(REAL, 'r') as f:
        lines = f.read().splitlines()
        for lineIdx, line in enumerate(lines):
            matches = re.finditer(r"\d+", line)
            print('inside ' + line)
            
            for match in matches:
                number = line[match.start(0):match.end(0)]
                print('looking for ' + number)
                # left
                left = match.start(0)-1
                if left >= 0 and line[left] != '.':
                    print('found ' + number)
                    acc += int(number)
                    continue
                
                # right
                right = match.end(0)
                if right < len(line) and line[right] != '.':
                    print('found ' + number)
                    acc += int(number)
                    continue
                                
                # above
                if lineIdx > 0:
                    prevLine = lines[lineIdx - 1]
                    st = max(match.start(0)-1, 0)
                    en = min(match.end(0) + 1, len(prevLine))
                    searchRegion = prevLine[st:en]
                    found = re.search(r"[^.]", searchRegion)
                    if found:
                        print('found ' + number)
                        acc += int(number)
                        continue
                    
                # below
                if lineIdx < len(lines) - 1:
                    nextLine = lines[lineIdx + 1]
                    st = max(match.start(0) - 1, 0)
                    en = min(match.end(0) + 1, len(nextLine))
                    searchRegion = nextLine[st:en]
                    print('search below ' + searchRegion + ' for ' + number)
                    found = re.search(r"[^.]", searchRegion)
                    if found:
                        print('found ' + number)
                        acc += int(number)
                        continue
                
                print('did not find ' + number)
    print(acc)

def solve2():
    acc = 0
    with open(REAL, 'r') as f:
        lines = f.read().splitlines()
        for lineIdx, line in enumerate(lines):
            print('Line: ' + line)
            
            stars = re.finditer("\*", line)
            for star in stars:
                print('star idx: %s' % star.start(0))
                touchings = []
                
                # same line
                numbers = re.finditer("\d+", line)
                for number in numbers:
                    numberStr = line[number.start(0):number.end(0)]
                    if number.end(0) == star.start(0):
                        touchings.append(int(numberStr))
                    if number.start(0) == star.end(0):
                        touchings.append(int(numberStr))
                
                # up line
                if lineIdx > 0:
                    above = lines[lineIdx-1]
                    numbers = re.finditer("\d+", above)
                    for number in numbers:
                        numberStr = above[number.start(0):number.end(0)]
                        r = range(number.start(0) - 1, number.end(0) + 1)
                        if star.start(0) in r:
                            touchings.append(int(numberStr))
                
                # down line
                if lineIdx < len(lines) - 1:
                    below = lines[lineIdx+1]
                    numbers = re.finditer("\d+", below)
                    for number in numbers:
                        numberStr = below[number.start(0):number.end(0)]
                        r = range(number.start(0) - 1, number.end(0) + 1)
                        if star.start(0) in r:
                            touchings.append(int(numberStr))
                            
                # and finally
                print('touchings: ' + str(touchings))
                if len(touchings) == 2:
                    acc += touchings[0] * touchings[1]
    print(acc)

    
if __name__ == "__main__":
    solve2()