import sys, re, json

def hw(sent_file, tweet_file):
    scores = sent_dict(sent_file)
    for tweet_str in tweet_file:
        tweet = json.loads(tweet_str)
        if not 'text' in tweet.keys(): continue # not a tweet
        text = tweet['text'].encode("utf-8")
        clean_text = re.sub('[^\w ]', '', text.lower())
        wrds = words_from_text(clean_text)
        score = tweet_score(wrds, scores)
        if score == 0: continue
        for word in wrds:
            if word not in scores:
                print word, float(score) / len(wrds)

def tweet_score(wrds, scores):
    wrds = filter(lambda x: len(x) != 0, wrds)
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
    return score

def words_from_text(text):
    return [s.strip() for s in text.split(' ')]

def bigrams(words):
    return [words[i]+' '+words[i+1]
            for i in range(len(words)-1)]

def trigrams(words):
    return [words[i]+' '+words[i+1]+' '+words[i+2]
            for i in range(len(words)-2)]

def sent_dict(sent_file):
    scores = {}
    for line in sent_file:
        term, score = line.split("\t")
        scores[term] = int(score)
    return scores

def lines(fp):
    print str(len(fp.readlines()))

def main():
    sent_file = open(sys.argv[1])
    tweet_file = open(sys.argv[2])
    hw(sent_file, tweet_file)

if __name__ == '__main__':
    main()
