import re

HERE = '/Users/Ethan/code/my-code/ProgrammingGit/StuffIWrote/Python/adventofcode/2013/inputs/'

def solve1():
    acc = 0
    with open(HERE + 'input1.txt', 'r') as f:
        for line in f:
            nums = re.sub(r"[^0-9]","", line)
            res = nums[0] + nums[len(nums)-1]
            acc += int(res)
            print(res)
    print(acc)


def solve2():
    sampleInput = """two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen"""
    acc = 0
    digitNames = ['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine']
    with open(HERE + 'input1.txt', 'r') as f:
        for line in f:
            
            fIdx = 1000000
            fName = ""
            fVal = -1
            for i, name in enumerate(digitNames):
                idxOf = line.find(name)
                if idxOf >= 0 and idxOf < fIdx:
                    fIdx = idxOf
                    fName = name
                    fVal = str(i + 1)
            if fName:
                print('fname', fName)
                line = line.replace(fName, fVal, 1)
            
            print('after f: ' + line)
            
            lIdx = -1
            lName = ""
            lVal = -1
            for i, name in enumerate(digitNames):
                idxOf = line.rfind(name)
                if idxOf > lIdx:
                    lIdx = idxOf
                    lName = name
                    lVal = str(i + 1)
            if lName:
                front, back = line[0:lIdx], line[lIdx:]
                back = back.replace(lName, lVal, 1)
                line = front + back
            
            nums = re.sub(r"[^0-9]","", line)
            res = nums[0] + nums[len(nums)-1]
            acc += int(res)
            print(res)
    print(acc)
   
if __name__ == "__main__":
    solve2()