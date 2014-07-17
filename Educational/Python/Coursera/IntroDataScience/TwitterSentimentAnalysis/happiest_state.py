# 7/11/14
# Ethan Petuchowski
# Yes, this file is a mess.
# Yes, I will never look at this file again.
# Yes, this file earned full-credit.

import sys, json, re

states = {
    'AK': 'Alaska',
    'AL': 'Alabama',
    'AR': 'Arkansas',
    'AS': 'American Samoa',
    'AZ': 'Arizona',
    'CA': 'California',
    'CO': 'Colorado',
    'CT': 'Connecticut',
    'DC': 'District of Columbia',
    'DE': 'Delaware',
    'FL': 'Florida',
    'GA': 'Georgia',
    'GU': 'Guam',
    'HI': 'Hawaii',
    'IA': 'Iowa',
    'ID': 'Idaho',
    'IL': 'Illinois',
    'IN': 'Indiana',
    'KS': 'Kansas',
    'KY': 'Kentucky',
    'LA': 'Louisiana',
    'MA': 'Massachusetts',
    'MD': 'Maryland',
    'ME': 'Maine',
    'MI': 'Michigan',
    'MN': 'Minnesota',
    'MO': 'Missouri',
    'MP': 'Northern Mariana Islands',
    'MS': 'Mississippi',
    'MT': 'Montana',
    'NA': 'National',
    'NC': 'North Carolina',
    'ND': 'North Dakota',
    'NE': 'Nebraska',
    'NH': 'New Hampshire',
    'NJ': 'New Jersey',
    'NM': 'New Mexico',
    'NV': 'Nevada',
    'NY': 'New York',
    'OH': 'Ohio',
    'OK': 'Oklahoma',
    'OR': 'Oregon',
    'PA': 'Pennsylvania',
    'PR': 'Puerto Rico',
    'RI': 'Rhode Island',
    'SC': 'South Carolina',
    'SD': 'South Dakota',
    'TN': 'Tennessee',
    'TX': 'Texas',
    'UT': 'Utah',
    'VA': 'Virginia',
    'VI': 'Virgin Islands',
    'VT': 'Vermont',
    'WA': 'Washington',
    'WI': 'Wisconsin',
    'WV': 'West Virginia',
    'WY': 'Wyoming'
}

def hw():
    afinnfile = open(sys.argv[1])
    scores = {}
    for line in afinnfile:
        term, score = line.split("\t")
        scores[term] = int(score)
    # print scores.items()
    ctr = {}
    tweet_file = open(sys.argv[2])
    for idx, tweet_str in enumerate(tweet_file):
        tweet = json.loads(tweet_str)

        # filter
        if not 'text' in tweet.keys(): continue # not a tweet
        if not 'user' in tweet.keys(): continue
        if not 'lang' in tweet['user'].keys(): continue
        if tweet['user']['lang'] != 'en': continue # toss non-English
        if not 'geo' in tweet.keys(): continue
        if not 'geo' in tweet.keys(): continue

        # get words
        text = tweet['text'].encode("utf-8")
        # if 'real fucking talk' in text:
        #     print 'HELLO!'
        clean_text = re.sub('[^\w ]', '', text.lower())
        wrds = words_from_text(clean_text)
        wrds = filter(lambda x: len(x) != 0, wrds)

        # get score
        bigs = bigrams(wrds)
        trigs = trigrams(wrds)
        score = 0
        i = 0
        while i < len(wrds):
            if i < len(trigs) and trigs[i] in scores:
                score += scores[bigs[i]]
                i += 2
            elif i < len(bigs) and bigs[i] in scores:
                score += scores[bigs[i]]
                i += 1
            elif wrds[i] in scores:
                score += scores[wrds[i]]
            i += 1

        ### get loc
        state = ''

        # state is given
        if 'place' in tweet.keys() and tweet['place'] is not None and 'full_name' in tweet['place'].keys():
            try_it = tweet['place']['full_name'][-2:]
            # if try_it == 'FL':
            #     print 'HELLO!'
            if try_it in states.keys():
                total, n = ctr.get(try_it, (0, 0))
                if n == 0:
                    ctr[try_it] = (score, 1)
                else:
                    ctr[try_it] = (total+score, n+1)

        # only coordinates are given
        else: continue

        # find the max
    avgs = [total/n for state, (total, n) in ctr.items()]
    names = ctr.keys()
    idx_max = max(enumerate(avgs), key=lambda x: x[1])[0]
    print names[idx_max]



def words_from_text(text):
    return [s.strip() for s in text.split(' ')]

def bigrams(words):
    return [words[i]+' '+words[i+1]
            for i in range(len(words)-1)]

def trigrams(words):
    return [words[i]+' '+words[i+1]+' '+words[i+2]
            for i in range(len(words)-2)]


def lines(fp):
    print str(len(fp.readlines()))

def main():
    sent_file = open(sys.argv[1])
    tweet_file = open(sys.argv[2])
    hw()
    # lines(sent_file)
    # lines(tweet_file)

if __name__ == '__main__':
    main()
