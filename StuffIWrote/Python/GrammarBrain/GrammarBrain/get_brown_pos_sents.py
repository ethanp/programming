import brown_pos_map as bpm
from nltk.corpus import brown

def get_brown_tagged_sents(MAX=15, MIN=1):
    return [s for s in brown.tagged_sents() if MIN <= len(s) < MAX and s[-1][0] == '.']

def count_categories_used(ss):  return len(set(w[1] for s in ss for w in s))

# transform sentences
#    from Brown's parts of speech (470 of them total)
#    to   more regular ones       ( 12 of them total)
def normalize_POSes(ss): return [[(w[0], bpm.pos_map[w[1]]) for w in s] for s in ss]

def vectorize_sents(ss, MAX_LEN=15):
    '''
    takes sentences with BROWN pos tags
    returns vectors of sentence's NORMAL pos tags
    output vectors must be of the required length for the ANN
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
    print vectorize_sents(get_brown_tagged_sents())[:2]
