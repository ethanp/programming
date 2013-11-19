from random import random, shuffle
import time

from pybrain.utilities import percentError
from pybrain.datasets.classification import SequenceClassificationDataSet
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.tools.validation import testOnSequenceData
from pybrain.tools.shortcuts import buildNetwork
from pybrain.structure import TanhLayer
        # TODO checkout the SharedFullConnection, LSTMLayer, BidirectionalNetwork, etc.


from util import brown_pos_map as bpm
from util.unpickle_pickles import print_sentence_range, get_sentence_matrices

GRAMMATICAL = (0, 1)
UNGRAMMATICAL = (1, 0)
MID_SENTENCE = (0.5, 0.5)

class GrammarTrainer(object):
    #noinspection PyTypeChecker
    def __init__(self, minim=4, maxim=5, outdim=2, hiddendim=5, train_time=50, basic_pos=True):
        # length restrictions on input sentences
        self.MIN_LEN, self.MAX_LEN = minim, maxim

        # number of different part of speech categorizations
        self.NUM_POS = len(bpm.pos_vector_map.keys()) if basic_pos else len(bpm.pos_map.keys())

        self.NUM_OUTPUTS, self.HIDDEN_SIZE = outdim, hiddendim
        self.network = self.build_sigmoid_network()
        self.training_iterations = train_time
        self.train_set, self.test_set = self.create_train_and_test_sets()


    def create_train_and_test_sets(self):

        def insert_grammatical_sequence(dataset, sentence_mat):
            dataset.newSequence()
            for i, word_vector in enumerate(sentence_mat):
                if i < len(sentence_mat) - 1:
                    dataset.appendLinked(word_vector, MID_SENTENCE)
                else:
                    dataset.appendLinked(word_vector, GRAMMATICAL)

        # there are a few options on what to do here it /would/ make sense to
        # give the first n-1 of these a `blank_label` like the grammatical
        # ones, but by /not/ doing that, we are assuming that we have no
        # problem declaring that partial sentences building up to ungrammatical
        # sentences should be already recognized as ungrammatical a happy
        # medium might be to label them as "probably" ungrammatical
        def insert_randomized_sequence(dataset, sentence_mat):
            dataset.newSequence()
            dup_sent_mat = sentence_mat[:]
            shuffle(dup_sent_mat)
            for word_vector in dup_sent_mat:
                dataset.appendLinked(word_vector, UNGRAMMATICAL)

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
        print '\nFirst 2 sentences of each length', self.MIN_LEN, 'and', self.MAX_LEN
        print '------------------------------------------------------------'
        print_sentence_range(self.MIN_LEN, self.MAX_LEN)
        print '------------------------------------------------------------'
        print '\nvectorizing sentences...'
        sentence_matrices = get_sentence_matrices(self.MIN_LEN, self.MAX_LEN)
        print '\ntotal number of sentences:', len(sentence_matrices)

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
        network = buildNetwork(self.NUM_POS, self.HIDDEN_SIZE, self.NUM_OUTPUTS,
                         bias=True, hiddenclass=TanhLayer, outclass=TanhLayer, recurrent=True)
        return network


    # http://pybrain.org/docs/api/supervised/trainers.html
    # backprop's "through time" on a sequential dataset
    def train(self, network_module, training_data, testing_data, n=20):
        trainer = BackpropTrainer(module=network_module, dataset=training_data, verbose=True)

        for i in range(n):
            trainer.trainEpochs(epochs=10)
            print 'epoch', i, 'finished'

            # modified from testOnClassData source code
            training_data.reset()
            print 'current fraction that are correct on training data:'
            print testOnSequenceData(network_module, training_data)

        print 'current fraction that are correct on testing data:'
        print testOnSequenceData(network_module, testing_data)

    def timed_train(self):
        start = time.clock()

        self.train(network_module=self.network,
                   training_data=self.train_set, testing_data=self.test_set,
                   n=1000)

        print time.clock() - start, 'seconds'



if __name__ == "__main__":
    gt = GrammarTrainer() # lots of params are supposed to go in here
    gt.timed_train()
