import nltk

with open('../Books/AliceInWonderland.txt', 'r') as book:
    # TODO alice in wonderland is not a good idea
    #   wikipedia is a better idea
    lines = book.readlines()
    for line in lines:
        print line.strip()
        tokens = nltk.word_tokenize(line)
        #print tokens