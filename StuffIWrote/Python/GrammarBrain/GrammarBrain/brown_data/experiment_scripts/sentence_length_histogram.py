import cPickle as pickle
import csv
import GrammarBrain.brown_data.util.get_brown_pos_sents as gbp

def double_filter_sentence_length_histogram():
    with open('../experiment_results/sentence_length_histogram.csv', 'wb') as out_file:
        writer = csv.writer(out_file)
        for i in range(2,25):
            print i
            words_file = '../brown_pickles/sentence_words_%d.p' % i
            writer.writerow([i, len(pickle.load(open(words_file, 'rb')))])

def filter_numbers_sentence_length_histogram():
    with open('../experiment_results/numbers_sentence_length_histogram.csv', 'wb') as out_file:
        writer = csv.writer(out_file)
        for i in range(3, 26): # want a range of 2 to 24, but min is 1 less than entered,
            sentences = gbp.get_nice_sentences(i, i+2) # max is 2 less than entered
            print i-1, len(sentences)
            writer.writerow([i-1, len(sentences)])

if __name__ == '__main__':
    filter_numbers_sentence_length_histogram()
