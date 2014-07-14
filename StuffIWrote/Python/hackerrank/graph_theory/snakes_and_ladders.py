'''
7/13/14, 5:17 PM
Ethan Petuchowski
snakes_and_ladders

https://www.hackerrank.com/challenges/the-quickest-way-up
'''
"""
There is one test-case on which this doesn't work.
It may be because I'm not handling the case where the
optimal strategy uses a snake. There are like 10 cases
where this does work, so it must be something strange
like that that I'm missing.

The problem with using the snake is that if you say
"I may have reached this square from a snake" then you
have to calculate how you reached the top of the snake.
But to do that you still have to calculate this square...
so it's turtles all the way down, and I'm not sure what
to do about it.
"""
class SnakeLadder(object):
    def __init__(self, snakes, ladders):
        assert isinstance(snakes, dict)
        assert isinstance(ladders, dict)
        self.snakes = snakes
        self.ladders = ladders
        self.cur_pos = 1
        self.mins = [0]+[1]*6+[100]*95

    def get_min(self, pos):
        if self.mins[pos] < 100:
            return self.mins[pos]

        if pos in self.ladders.keys():
            self.mins[pos] = self.get_min(self.ladders[pos])
            return self.mins[pos]

        if pos in self.snakes.keys():
            return self.mins[pos]

        m = self.mins[pos]
        for i in range(1, 7):
            r = 1 + self.get_min(pos - i)
            if r < m: m = r
        self.mins[pos] = m
        return self.mins[pos]


def main():
    num_cases = input()
    for case in range(num_cases):
        num_ladders, num_snakes = map(int, raw_input().split(','))
        ladder_endpoints = map(int, raw_input().replace(' ',',').split(','))
        snake_endpoints = map(int, raw_input().replace(' ',',').split(','))

        ladders = {}
        for s in range(0, len(ladder_endpoints), 2):
            ladders[ladder_endpoints[s+1]] = ladder_endpoints[s]

        snakes = {}
        for s in range(0, len(snake_endpoints), 2):
            snakes[snake_endpoints[s]] = snake_endpoints[s+1]

        case = SnakeLadder(snakes, ladders)
        print case.get_min(100)

if __name__ == '__main__':
    main()
