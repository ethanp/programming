from collections import Counter

from nltk.corpus import brown
from nltk.tag.simplify import simplify_brown_tag as sbt
import nltk


def nltk_pos_tag_default():
    ''' Produces ~36 POS tags '''
    new_pos_tagged_sents = [nltk.pos_tag(sent) for sent in brown.sents()[:300]]

    ctr = Counter()

    for sent in new_pos_tagged_sents:
        for word, pos in sent:
            ctr[pos] += 1

    print len(ctr)
    return ctr

def nltk_simplify_brown_tag():
    ''' Produces ~36 POS tags '''
    other_pos_tagging = [sbt(tag) for (_, tag) in brown.tagged_words()[:600000]]
    ctr = Counter()
    for tag in other_pos_tagging:
        ctr[tag] += 1

    print len(ctr)
    return ctr

if __name__ == '__main__':

    ctr1 = nltk_pos_tag_default()
    ctr2 = nltk_simplify_brown_tag()

    print sorted(ctr1.keys())
    print sorted(ctr2.keys())

