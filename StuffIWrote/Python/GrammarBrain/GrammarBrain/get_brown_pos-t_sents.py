import brown_pos_map as bpm
from nltk.corpus import brown

def get_tagged_sents(MAX=15, MIN=1):
    return [s for s in brown.tagged_sents() if MIN <= len(s) < MAX and s[-1][0] == '.']

def count_categories_used(ss):  return len(set(w[1] for s in ss for w in s))

# transform sentences
#    from Brown's parts of speech (470 of them total)
#    to   more regular ones       ( 13 of them total)
def normalize_POSes(ss): return [[(w[0], bpm.pos_map[w[1]]) for w in s] for s in ss]

# for trying it out
if __name__ == "__main__":  print normalize_POSes(get_tagged_sents())[:2]
