# 7/10/14
# Ethan Petuchowski

# Compute the "term frequency histogram"
# i.e.:
# [# of occurrences of the term in all tweets]
# --------------------------------------------
# [# of occurrences of all terms in all tweets]

import json, sys, re
from collections import Counter


def hist(tweet_file):
    ctr = Counter()
    for tweet_str in tweet_file:
        tweet = json.loads(tweet_str)
        if not 'text' in tweet.keys(): continue  # not a tweet
        text = tweet['text'].encode("utf-8")
        clean_text = re.sub('[^\w ]', '', text.lower())
        wrds = words_from_text(clean_text)
        for w in wrds:
            ctr[w] += 1
    wc = sum(v for v in ctr.values())
    for k, v in ctr.iteritems():
        print k, float(v)/wc

def words_from_text(text):
    return [s.strip() for s in text.split(' ')]

def main():
    tweet_file = open(sys.argv[1])
    hist(tweet_file)

if __name__ == '__main__':
    main()
