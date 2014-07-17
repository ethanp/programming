# 7/11/14
# Ethan Petuchowski
# Compute the ten most frequently occurring hashtags

import json, sys
from collections import Counter

def main():
    tweet_file = open(sys.argv[1])
    ctr = Counter()
    for tweet_str in tweet_file:
        tweet = json.loads(tweet_str)

        # is there a cleaner way?
        hashtags = tweet.get('entities', {}).get('hashtags', [])
        for h in hashtags:
            ctr[h['text']] += 1

    # look ma, no stack overflow!
    s = sorted(ctr.items(), key=lambda x: x[1])
    for i, j in s[:10]:
        print i, j

if __name__ == '__main__':
    main()
