import cPickle as pickle

def print_example_sentences(length, n=5):
    assert 1 < length < 25
    words_file = '../pickles/sentence_words_%d.p' % length
    if n == 1:
        print pickle.load(open(words_file, 'rb'))[0]
    else:
        print pickle.load(open(words_file, 'rb'))[:n]

def print_sentence_range(MIN, MAX):
    for i in range(MIN, MAX+1):
        print_example_sentences(i, 2)

if __name__ == "__main__":
    print_sentence_range(2, 10)
