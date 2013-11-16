import brown_pos_map as bpm
from nltk.corpus import brown

def get_brown_tagged_sents(MAX=8, MIN=3):
    '''
    get sentences from the Brown dataset and their associated POS tags
    only get sentences within the specified range bounds that end in a period
    strip off the final period to simplify the overall learning task
    '''
    # TODO this could probably just return a generator
    return [s[:-1] for s in brown.tagged_sents() if MIN <= len(s) < MAX and s[-1][0] == '.']

def get_nice_sentences(MAX=8, MIN=3):
    return filter_punctuation(filter_numbers(get_brown_tagged_sents(MAX, MIN)))

def count_categories_used(ss):  return len(set(w[1] for s in ss for w in s))

def normalize_POSes(ss):
    '''
    transform sentences
        from Brown's parts of speech (470 of them total)
        to   more 'Normal' ones      ( 12 of them total)
    '''
    # TODO this could probably just return a generator
    return [[(word, bpm.pos_map[pos]) for word, pos in s] for s in ss]


def sentence_strings(ss, n=10):
    ''' print the actual sentences that were collected (without POSs) '''
    # TODO this could probably just return a generator
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
            # TODO I'm sure this would be much faster with numpy...
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
    sentences = get_nice_sentences()
    #print normalize_POSes(get_brown_tagged_sents())[:2]
    print construct_sentence_matrices(sentences)[:2]
    print_n_sentences(sentences)
