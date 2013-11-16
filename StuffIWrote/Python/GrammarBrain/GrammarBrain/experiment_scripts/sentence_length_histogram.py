import cPickle as pickle
import csv

with open('../experiment_results/sentence_length_histogram.csv', 'wb') as out_file:
    writer = csv.writer(out_file)
    for i in range(2,25):
        print i
        words_file = '../pickles/sentence_words_%d.p' % i
        writer.writerow([i, len(pickle.load(open(words_file, 'rb')))])
