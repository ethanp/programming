def get_tagged_sents(MAX_LENGTH, MIN_LENGTH=1):
    '''
    only return sentences that end in a period (i.e. no questions, etc.)
    '''
    from nltk.corpus import brown
    return [s for s in brown.tagged_sents()
            if MIN_LENGTH <= len(s) <= MAX_LENGTH
                and s[-1][0] == '.']


def count_categories(sents):
    categs = set([])
    for sent in sents:
        for word in sent:
            categs.add(word[1])
    print categs
    return len(categs)


def normalize_POSes(sents):
    '''
    transform sentences
        from Brown's parts of speech
        to more regular ones

    '''
    import brown_pos_map as bpm
    return [[(word[0], bpm.pos_map[word[1]]) for word in sent] for sent in sents]



if __name__ == "__main__":
    MAX_LENGTH = 13
    ss = get_tagged_sents(MAX_LENGTH)
    #count_categories(ss)
    nss = normalize_POSes(ss)
    print nss[:2]
