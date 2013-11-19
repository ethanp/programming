from random import sample, random, shuffle
from pybrain.utilities import percentError
from numpy import argmax
from get_brown_pos_sents import get_nice_sentences, construct_sentence_matrices, print_n_sentences
import brown_pos_map as bpm
import time
from pybrain.datasets.classification import SequenceClassificationDataSet
import cPickle as pickle

GRAMMATICAL = (0, 1)
UNGRAMMATICAL = (1, 0)
MID_SENTENCE = (0.5, 0.5)

class GrammarTrainer(object):
    #noinspection PyTypeChecker
    def __init__(self, minim=4, maxim=5, outdim=2, hiddendim=5, train_time=50, basic_pos=True):
        # length restrictions on input sentences
        self.MIN_LEN = minim
        self.MAX_LEN = maxim

        # number of different part of speech categorizations
        if basic_pos:
            self.NUM_POS = len(bpm.pos_vector_map.keys())
        else:
            self.NUM_POS = len(bpm.pos_map.keys())
        self.NUM_OUTPUTS = outdim
        self.HIDDEN_LAYER_SIZE = hiddendim
        self.network = None
        self.training_iterations = train_time
        self.train_set, self.test_set = self.create_train_and_test_sets()


    def create_train_and_test_sets(self, MIN_LEN=None, MAX_LEN=None):
        if MIN_LEN is None:
            MIN_LEN = self.MIN_LEN
        if MAX_LEN is None:
            MAX_LEN = self.MAX_LEN

        # TODO this needs to be replaced with insert_sequence_vsn_3() from train_trial.py
        def insert_grammatical_sequence(dataset, sentence_mat):
            dataset.newSequence()
            for i, word_vector in enumerate(sentence_mat):
                if i < len(sentence_mat) - 1:
                    dataset.appendLinked(word_vector, [0])
                else:
                    dataset.appendLinked(word_vector, [1])

        def insert_randomized_sequence(dataset, sentence_mat):
            dataset.newSequence()
            dup_sent_mat = sentence_mat[:]
            shuffle(dup_sent_mat)
            for word_vector in dup_sent_mat:
                dataset.appendLinked(word_vector, [0])

        def print_data_data(data, name):
            print "num", name, "patterns: ", len(data)
            print "input and output dimensions: ", data.indim, data.outdim
            print "First sample (input, target, class):"
            print data['input'][0], data['target'][0], data['class'][0]

        # inp: dimensionality of the input (# of POS types)
        # target: output dimensionality (# of possible classifications)
        train_data = SequenceClassificationDataSet(inp=self.NUM_POS, target=2)
        test_data = SequenceClassificationDataSet(inp=self.NUM_POS, target=2)

        # brown dataset, no mid-sentence punctuation, no numbers, ends in period, within length range
        # TODO this should be unpickling my pickles
        sentences = get_nice_sentences(MAX_LEN, MIN_LEN)
        print '\ntotal number of sentences:', len(sentences)
        # TODO this could use the sentence-sampler
        print '\nFirst five sentences between length', MIN_LEN-1, 'and', MAX_LEN-2
        print '------------------------------------------------------------'
        print_n_sentences(sentences, n=5)
        print '------------------------------------------------------------'
        print '\nvectorizing sentences...'
        # TODO this should be unpickling my pickles
        sentence_matrices = construct_sentence_matrices(sentences)

        print 'creating training and test sets...'
        for sentence_matrix in sentence_matrices:
            if random() < .25:  # percent distribution between sets needn't be perfect, right?
                insert_grammatical_sequence(test_data, sentence_matrix)
                insert_randomized_sequence(test_data, sentence_matrix)

            else:
                insert_grammatical_sequence(train_data, sentence_matrix)
                insert_randomized_sequence(train_data, sentence_matrix)

        ''' FOR DEBUGGING DATASET '''
        #print_data_data(train_data, 'training')
        #print_data_data(test_data, 'testing')

        return train_data, test_data


    def build_sigmoid_network(self):
        from pybrain.structure import TanhLayer, LSTMLayer
        # TODO checkout the SharedFullConnection, LSTMLayer, BidirectionalNetwork, etc.

        from pybrain.tools.shortcuts import buildNetwork
        network = buildNetwork(NUM_REG_POS, HIDDEN_LAYER_SIZE, NUM_OUTPUTS,
                         bias=True, hiddenclass=LSTMLayer, outclass=TanhLayer, recurrent=True)

        # buildNetwork() calls sortModules() at the end on its own

        return network


    # http://pybrain.org/docs/api/supervised/trainers.html
    # backprop's "through time" on a sequential dataset
    def train(self, network_module, training_data, testing_data, n=20):
        from pybrain.supervised.trainers import BackpropTrainer
        trainer = BackpropTrainer(module=network_module, dataset=training_data, verbose=True)
        training_size = training_data.getNumSequences()

        for i in range(n):
            trainer.trainEpochs(epochs=1)
            print 'epoch', i, 'finished'

            # modified from testOnClassData source code
            training_data.reset()
            num_correct, print_counter, estimated_class, true_class = 0, 0, [], []
            for seq in training_data._provideSequences():
                trainer.module.reset()
                a = True
                for inp, target in seq:
                    if a:
                        t = target
                        a = False
                    res = trainer.module.activate(inp)
                #if p < 20:
                #    print 'target:', t
                #    print 'result:', res
                #    p += 1
                estimated_class.append(argmax(res))
                true_class.append(argmax(t))
                if estimated_class[-1] == true_class[-1]:
                    num_correct += 1
            print_counter += 1
            if print_counter % 20 == 0:
                print 'training error =', num_correct, '/', training_size

    def go(self):
        train_data, test_data = self.create_train_and_test_sets(MIN_LEN, MAX_LEN)
        network = self.build_sigmoid_network()
        start = time.clock()
        self.train(network_module=network, training_data=train_data, testing_data=test_data, n=1000)
        print time.clock() - start, 'seconds'

if __name__ == "__main__":
    gt = GrammarTrainer() # lots of params are supposed to go in here
    gt.go()
