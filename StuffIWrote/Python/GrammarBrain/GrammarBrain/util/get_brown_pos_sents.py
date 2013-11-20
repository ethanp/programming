from nltk.corpus import brown, semcor, conll2000, treebank
import sys

from GrammarBrain.util import brown_pos_map as bpm


CORPORA = brown, semcor, conll2000, treebank

def test_corpus_intersections(MIN=3, MAX=3):
    sets = []
    for corpus in CORPORA:
        print corpus.__repr__()
        a_set = set()
        counter = 0
        for a in corpus.tagged_sents():
            if MIN < len(a) <= MAX + 1 and a[-1][0] == '.':
                words = ' '.join([w[0] for w in a[:-1]])
                a_set.add(words)
                if counter < 5:
                    print words
                    sys.stdout.flush()
                    counter += 1
        sets.append((a_set, str(corpus)))

        print corpus.__repr__()

    for b in sets[::-1]:
        print b[1]
        print str(len(b[0])) + '\n'
        for c in sets:
            if b is not c:
                print 'with', c[1]
                d = b[0].copy()
                d = d.difference(c[0])
                print len(d)
        print '\n-------------------------------------------------------------'


def get_many_tagged_sents(MIN=3, MAX=5):
    # getting length of iterator does not compute
    return [s[:-1] for corpus in CORPORA for s in corpus.tagged_sents()
            if MIN < len(s) <= MAX + 1 and s[-1][0] == '.']

def get_brown_tagged_sents(MIN=3, MAX=4):
    '''
    get sentences from the Brown dataset and their associated POS tags
    only get sentences within the specified range bounds that end in a period
    strip off the final period to simplify the overall learning task
    the sentence sizes are INCLUSIVE
    '''
    return (s[:-1] for s in brown.tagged_sents()
            if MIN < len(s) <= MAX+1 and s[-1][0] == '.')

def get_nice_sentences(MIN=3, MAX=4, include_numbers=False):
    if include_numbers:
        return filter_punctuation(get_brown_tagged_sents(MIN, MAX))
    else:
        return filter_punctuation(filter_numbers(get_brown_tagged_sents(MIN, MAX)))

def count_categories_used(ss):  return len(set(w[1] for s in ss for w in s))

def normalize_POSes(ss):
    '''
    transform sentences
        from Brown's parts of speech (470 of them total [no joke])
        to   more 'Normal' ones      ( 12 of them total)
    '''
    return ([(word, bpm.pos_map[pos]) for word, pos in s] for s in ss)


def sentence_strings(ss, n=10):
    ''' print the actual sentences that were collected (without POSs) '''
    # can't do indexing on generator
    return [" ".join(w[0] for w in s) for s in ss][:n]

def filter_punctuation(ss):
    def remove_punctuation(sentence):
        for i, (word, pos) in enumerate(sentence):
            if bpm.brown_index(pos) == 0: # zero means punctuation
                return False
        return True
    return filter(remove_punctuation, ss)

def filter_numbers(ss):
    def remove_numbers(sentence):
        for word, pos in sentence:
            if bpm.brown_index(pos) == 11:  # eleven means number
                return False
        return True
    return filter(remove_numbers, ss)

def construct_sentence_matrices(ss):
    '''
    takes sentences with BROWN pos tags
    returns matrices of sentence's NORMAL pos tags
    output matrices NEED NOT be of the required length for the ANN
        however each word-vector MUST be of the required length == len(normal POS tags)
    '''
    sentence_matrices = []
    for s in ss:
        sentence_matrix = []
        for w in s:
            # this would be much faster with numpy, but with pickling, who cares
            word_vector = [0] * len(bpm.pos_vector_map.keys())
            word_vector[bpm.brown_index(w[1])] = 1
            sentence_matrix.append(word_vector)
        sentence_matrices.append(sentence_matrix)
    return sentence_matrices

def print_n_sentences(ss, n=15):
    for sentence in sentence_strings(ss, n=n):
        print sentence

# for trying it out
if __name__ == "__main__":
    #sentences = filter_punctuation(get_brown_tagged_sents(4, 6))
    #print_n_sentences(sentences)
    #print len(get_many_tagged_sents(3,3))
    test_corpus_intersections()