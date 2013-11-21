from collections import Counter
import wiki_pos_map
import sys, os
import cPickle as pickle
import multiprocessing
from time import time

path = '/Users/Ethan/Desktop/Data/wiki_tagged.en/'
base_name = '/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/Python/GrammarBrain/GrammarBrain/wiki_data/wiki_pickles/'
tuple_file = base_name+'tuple_'
sentence_file = base_name+'sentence_'
pos_file = base_name+'pos_'
matrix_file_of_len = base_name+'matrix_'
ALL_OF_THEM = 1000


def import_n_wiki_to_tuple_files(n=2):
    file_count = 0
    sents = []
    for filenum, filename in enumerate(os.listdir(path)):
        if file_count < n:
            file_count += 1
            print filename
            with open(path+filename, 'rb') as text:
                s = []
                lines = text.readlines()
                print len(lines)
                if len(lines) < 3:
                    # blank file
                    break
                for line in lines:
                    if line[0] == '<':
                        # meta-data
                        continue
                    a = line.split(' ')
                    if len(a) > 2:
                        # collect sentence
                        s.append((a[0], a[2]))
                    else:
                        # end of sentence
                        # save sentence, etc.
                        sents.append(s[:])
                        s = []

                print 'len(sents)', len(sents)
                sents = filter(filter_invalid_POSs, sents)
                print 'len(sents)', len(sents)
                print sents[:2]
                for length in range(2, 25):
                    sents_of_length_j = [s for s in sents if len(s) == length]
                    print length, ',', len(sents_of_length_j)
                    out_file = sentence_file + str(length) + '_' + str(filenum)
                    if len(sents_of_length_j) > 10:
                        print sents_of_length_j[10]
                    elif len(sents_of_length_j):
                        print sents_of_length_j[0]
                    else:
                        print 'why am I in here?', filename, length
                    pickle.dump(sents_of_length_j, open(out_file, 'wb'))

                sents = []

# using this in the way I have means that there
# exist sentences with no corresponding matrix
def filter_invalid_POSs(s):
    for w, p in s:
        if p not in wiki_pos_map.the_map.keys():
            return False
    return True


def concurrently_create_matrices_and_sentence_pickles():
    TOTAL_LENGTH = 25 - 2
    PROCESSES = 0
    num_per_process = TOTAL_LENGTH / PROCESSES + 1  # 'cause that's what they say
    start = time()

    pool = multiprocessing.Pool(processes=PROCESSES,
                                initializer=start_process,
                                maxtasksperchild=num_per_process)

    # TODO not done with the parameters here yet
    pool.map(pooled_create_matrices_and_sentence_pickles_of_length)


def pooled_create_matrices_and_sentence_pickles_of_length(length):
    print 'length', length
    sentence_matrices = []
    for filenum in range(len(os.listdir(path))):
        in_file = sentence_file + str(length) + '_' + str(filenum)
        if os.path.exists(in_file):
            print 'filenum', filenum
            sentences = pickle.load(open(in_file, 'rb'))
            for sentence in sentences:
                sentence_matrix = []
                word_vec = [0] * len(wiki_pos_map.the_map)
                for word, pos in sentence:
                    # create word vector
                    this_vec = word_vec[:]
                    this_vec[wiki_pos_map.the_map[pos]] = 1
                    sentence_matrix.append(this_vec)
                    del this_vec
                sentence_matrices.append(sentence_matrix[:])
    out_file = matrix_file_of_len + str(length) + '.p'
    pickle.dump(sentence_matrices, open(out_file, 'wb'))


def start_process():
    print 'Starting', multiprocessing.current_process().name

# go through sentences, and put it in 'file_%d' % len(sentence)
# if len(sentence) < 25
if __name__ == '__main__':
    concurrently_create_matrices_and_sentence_pickles()
