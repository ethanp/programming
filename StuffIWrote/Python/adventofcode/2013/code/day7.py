from pathlib import Path
from collections import defaultdict

cardRanks = ['A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2']

class Hand(object):
    def __init__(self, cards):
        self.cards = cards

    def counts(self):
        counter = defaultdict(int)
        for card in self.cards:
            counter[card] += 1
        return counter

    def matches(self):
        pass

    def __lt__(self, other):
        return self.compare(other) < 0

    def compare(self, other):
        # noinspection PyTypeChecker
        classWise = typeOrder.index(self.__class__) \
                    - typeOrder.index(other.__class__)
        if classWise != 0:
            return classWise
        for mine, theirs in zip(self.cards, other.cards):
            comp = cardRanks.index(mine) - cardRanks.index(theirs)
            if comp != 0:
                return comp
        return 0

class Five(Hand):
    def matches(self):
        return len(self.counts()) == 1

class Four(Hand):
    def matches(self):
        return 4 in self.counts().values()

class Full(Hand):
    def matches(self):
        c = self.counts()
        return set(c.values()) == {2, 3}

class Three(Hand):
    def matches(self):
        return 3 in self.counts().values()

class TwoPair(Hand):
    def matches(self):
        return sorted(self.counts().values()) == [1, 2, 2]

class OnePair(Hand):
    def matches(self):
        return sorted(self.counts().values()) == [1, 1, 1, 2]

class High(Hand):
    def matches(self):
        return max(self.counts().values()) == 1

typeOrder = [Five, Four, Full, Three, TwoPair, OnePair, High]

def solve1(lines):
    hands = []
    for line in lines:
        split = line.split(' ')
        cards, bid = split[0], int(split[1])
        for t in typeOrder:
            tCards = t(cards)
            if tCards.matches():
                hands.append((tCards, bid))
                break
    # in-place, increasing rank
    hands.sort(key=lambda x: x[0], reverse=True)
    acc = 0
    for idx, (_, bid) in enumerate(hands):
        acc += (idx + 1) * bid
    print('result: %d' % acc)

def possibilities(withoutJs):
    if len(withoutJs) == 5:
        return [withoutJs]
    if len(withoutJs) == 4:
        return (withoutJs + v for v in cardRanks)
    if len(withoutJs) == 3:
        return (withoutJs + ''.join((a, b))
                for a in cardRanks
                for b in cardRanks)
    if len(withoutJs) == 2:
        return (withoutJs + ''.join((a, b, c))
                for a in cardRanks
                for b in cardRanks
                for c in cardRanks)
    return 'AAAAA'

def tryAllJokerValues(handType, withoutJs):
    for poss in possibilities(withoutJs):
        if handType(poss).matches():
            return True
    return False

def solve2(lines):
    global cardRanks  # override
    cardRanks = ['A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J']
    hands = []
    for line in lines:
        split = line.split(' ')
        cards, bid = split[0], int(split[1])
        withoutJs = cards.replace('J', '')
        for handType in typeOrder:
            if tryAllJokerValues(handType, withoutJs):
                hands.append((handType(cards), bid))
                break

    # in-place, increasing rank
    hands.sort(key=lambda x: x[0], reverse=True)
    acc = 0
    for idx, (_, bid) in enumerate(hands):
        print(idx, _.cards, bid)
        acc += (idx + 1) * bid

    print('result: %d' % acc)

SAMPLE = 'sample'
ACTUAL = 'actual'

whichInput = ACTUAL
whichLevel = solve2

if __name__ == "__main__":
    dayNum = __file__.split('/')[-1].split('.')[0][3:]
    filename = 'input%s.txt' % dayNum
    inputFile = Path(__file__).parents[1] / 'inputs' / whichInput / filename
    with open(inputFile, 'r') as f:
        whichLevel(f.readlines())
