# coding=utf-8
# 6/24/14 -- 9:38 - 10:05
# Ethan Petuchowski
# Speed-code, no planning

import random

COLORS = ["red", "blue", "green", "orange"]
num_colors = len(COLORS)

class Mastermind(object):
    def __init__(self, size):
        self.hidden = []
        for i in range(size):
            self.hidden.append(COLORS[int(random.random() * num_colors)])
        print 'hidden:', self.hidden
        self.size = len(self.hidden)
        self.num_guesses = 0
        self.won = False

    def guess(self, colors):
        assert isinstance(colors, list), 'must guess a list of colors'

        if not len(colors) == self.size:
            print 'Please guess %d colors' % self.size
            return

        blacks = 0
        whites = 0

        hidden_without_blacks = self.hidden[:]
        guess_without_blacks = colors[:]

        # count blacks
        for i in range(self.size):

            if not colors[i] in COLORS:
                print 'guessed invalid color: %s' % colors[i]
                return

            if colors[i] == self.hidden[i]:
                blacks += 1
                hidden_without_blacks[i] = None
                guess_without_blacks[i] = None

        if blacks == len(self.hidden):
            print 'YOU WIN!'
            print 'You took', self.num_guesses + 1, 'guesses'
            self.won = True
            return

        # count whites
        for i in range(self.size):
            if colors[i] and colors[i] in hidden_without_blacks:
                whites += 1
                hidden_without_blacks[hidden_without_blacks.index(colors[i])] = None

        self.num_guesses += 1
        print 'blacks:', blacks, 'whites:', whites, 'guesses:', self.num_guesses

    def play(self):
        while not self.won:
            user_guess = raw_input('Enter %d colors, like "%s":'
                                   % (self.size, ('green '* self.size)[:-1]))
            colors = [g.strip() for g in user_guess.split(' ')]
            self.guess(colors)

if __name__ == '__main__':
    Mastermind(size=4).play()
