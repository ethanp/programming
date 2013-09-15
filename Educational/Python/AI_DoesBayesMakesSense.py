"""
 4/5/13 - After doing the Reading of Ch 20-.2 of AIMA for AI class

 My question is this:
    Let's say I have a coin that lands on heads 70% of the time,
      and I'm going to bet on its outcome 1000 times.
    Would I be better off betting on heads 100% of the time or 70% of the time?

 Results:
    I would be much better off betting on heads 100% of the time.

    I could have gotten this faster by noting that
        P(H,BetH)+P(T,BetT) = .7^2+.3^2
                            = .49+.09
                            = .58 < .7 = P(H)*1 + P(T)*0

"""

from random import randint

def main():
    heads = 0
    statistics = 0
    for i in xrange(1000):
        coin = randint(1,10)
        statistician = randint(1,10)
        if coin <= 7:
            heads += 1
            if statistician <= 7:
                statistics += 1
        else:
            if statistician > 7:
                statistics += 1

    print 'heads: ', heads
    print 'statistics: ', statistics


if __name__ == "__main__":
    main()
