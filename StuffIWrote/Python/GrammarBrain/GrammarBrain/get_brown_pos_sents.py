import brown_pos_map as bpm
from nltk.corpus import brown

def get_brown_tagged_sents(MAX=15, MIN=1):
    return [s for s in brown.tagged_sents() if MIN <= len(s) < MAX and s[-1][0] == '.']

def count_categories_used(ss):  return len(set(w[1] for s in ss for w in s))

# transform sentences
#    from Brown's parts of speech (470 of them total)
#    to   more regular ones       ( 12 of them total)
def normalize_POSes(ss): return [[(word, bpm.pos_map[pos]) for word, pos in s] for s in ss]

# print the actual sentences that were collected (without POSs)
def sentence_strings(ss, n=10):  return [" ".join(w[0] for w in s) for s in ss][:n]

# filter sentences with punctuation in the middle
def filter_punctuation(ss):
    def remove_punctuation(sentence):
        # return false if punctuation in sentence
        for i, (word, pos) in enumerate(sentence):
            # zero means punctuation, not the final period
            if bpm.brown_index(pos) == 0 and i < len(sentence)-1:
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

def vectorize_sents(ss, MAX_LEN=15):
    '''
    takes sentences with BROWN pos tags
    returns vectors of sentence's NORMAL pos tags
    output vectors MUST be of the required length for the ANN (padded with -1's)
    '''
    # TODO I'm sure this would be much faster with numpy...
    sentence_vectors = []
    for s in ss:
        sentence_vector = [0] * MAX_LEN
        for i in range(MAX_LEN):
            if i < len(s):
                sentence_vector[i] = bpm.brown_index(s[i][1])
            else:
                sentence_vector[i] = -1
        sentence_vectors.append(sentence_vector)
    return sentence_vectors


# for trying it out
if __name__ == "__main__":
    #print normalize_POSes(get_brown_tagged_sents())[:2]
    #print vectorize_sents(get_brown_tagged_sents())[:2]
    for s in sentence_strings(
        filter_numbers(
            filter_punctuation(
                get_brown_tagged_sents(MAX=8)))):
        print s