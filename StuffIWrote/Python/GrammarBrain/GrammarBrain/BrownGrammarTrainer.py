from random import random, shuffle
import time

from pybrain.datasets.classification import SequenceClassificationDataSet
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.tools.validation import testOnSequenceData
from pybrain.tools.shortcuts import buildNetwork
from pybrain.structure.connections import FullConnection
from pybrain.structure import TanhLayer, LSTMLayer
# checkout the SharedFullConnection, LSTMLayer, BidirectionalNetwork, etc.
# see if RPROP works faster


from brown_data.util.unpickle_brown_pickles import print_sentence_range, get_sentence_matrices
from brown_data.util.brown_pos_map import pos_reducer, pos_vector_mapping


GRAMMATICAL = (0, 1)
UNGRAMMATICAL = (1, 0)
MID_SENTENCE = (0.5, 0.5)

# TODO be able to print the thing for graphing use
'''
    HOW_TO:
    =====

 1. load some data-structure (dict?) with the hyperparameters

 2. as the thing is training, be concatenating some list with
    the error-numbers over the training, (validation?,) and test sets

 3. print the param_dict as `comma_separated(keys())\ncomma_separated(values())`

 4. print the training as

    epoch,    training error,   testing error
     1,          .4,              .5
     2,          .35,             .49
     ...,        ...,             ...


'''
class BrownGrammarTrainer(object):
    #noinspection PyTypeChecker
    def __init__(self, minim=4, maxim=5, outdim=2, hiddendim=None,
                 train_time=50, basic_pos=True, hidden_type=LSTMLayer,
                 output_type=TanhLayer):
        if not hiddendim: hiddendim = [5]
        self.MIN_LEN, self.MAX_LEN = minim, maxim
        self.NUM_POS = len(pos_vector_mapping) if basic_pos else len(pos_reducer)
        self.basic_pos = basic_pos
        self.NUM_OUTPUTS, self.HIDDEN_LIST = outdim, hiddendim
        self.HIDDEN_TYPE = hidden_type
        self.OUTPUT_TYPE = output_type
        self.network = self.build_network()
        self.training_iterations = train_time
        print str(self)
        self.train_set, self.test_set = self.create_train_and_test_sets()
        self.repr_dict = {
            'min_len': self.MIN_LEN,
            'max_len': self.MAX_LEN,
            'num_pos': self.NUM_POS,
            'hidden_list': self.HIDDEN_LIST,
            'hidden_type': self.HIDDEN_TYPE,
            'output_type': self.OUTPUT_TYPE,
            'training_iterations': self.training_iterations
        }

        # TODO fill this in during training
        self.train_dict = {}

    def __str__(self):
        string = ['BROWN DATASET']
        string += ['Sentences of length {0} to {1}'.format(str(self.MIN_LEN), str(self.MAX_LEN))]
        pos_set = 'BASIC' if self.basic_pos else 'EXTENDED'
        string += ['Using {0} pos set'.format(pos_set)]
        string += ['Hidden size: {0}'.format(str(self.HIDDEN_LIST))]
        string += ['Hidden type: {0}'.format(str(self.HIDDEN_TYPE))]
        string += ['Number of training iterations: {0}'.format(str(self.training_iterations))]
        string += ['\n-------------------------------------------------------']
        string += ['Network Layout']
        string += ['-------------------------------------------------------']
        for module in self.network.modules:
            string += ['\n' + str(module)]
            for connection in self.network.connections[module]:
                string += [str(connection)]
        string += ['\nRecurrent connections']
        for connection in self.network.recurrentConns:
            string += [str(connection)]
        string += ['\n-------------------------------------------------------\n']
        return '\n'.join(string)


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
        # TODO need to create a validation set too?
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


    def build_network(self):
        network_options = {
            'hiddenclass' : self.HIDDEN_TYPE,
            'outclass' : self.OUTPUT_TYPE,
            'recurrent' : True,
            'bias' : True
        }
        layout = tuple([self.NUM_POS] + self.HIDDEN_LIST + [self.NUM_OUTPUTS])
        network = buildNetwork(*layout, **network_options)

        # these are the default "module" names
        # NOTE: you DO have to add a hidden->hidden connection even when you set rec=True
        #   bc otw how would it know that you wanted that /particular/ connection!?
        h = network['hidden0']
        o = network['out']
        network.addRecurrentConnection(FullConnection(h, h))
        network.addRecurrentConnection(FullConnection(o, h))
        network.sortModules()
        return network


    # http://pybrain.org/docs/api/supervised/trainers.html
    # backprop's "through time" on a sequential dataset
    def train(self, network_module, training_data, testing_data, n=20, s=5):
        trainer = BackpropTrainer(module=network_module, dataset=training_data, verbose=True)
        for i in range(n/s):
            trainer.trainEpochs(epochs=s)
            print 'epoch', (i+1)*s, 'finished'

            # modified from testOnClassData source code
            training_data.reset()
            print '\nTRAINING: {:.2f}% correct'.format(
                testOnSequenceData(network_module, training_data) * 100)

            print 'TESTING: {:.2f}% correct\n'.format(
                testOnSequenceData(network_module, testing_data) * 100)


    def timed_train(self):
        start = time.clock()

        self.train(network_module=self.network,
                   training_data=self.train_set, testing_data=self.test_set,
                   n=self.training_iterations)

        print '%.2f minutes' % ((time.clock() - start)/60)


if __name__ == "__main__":
    gt = BrownGrammarTrainer(hiddendim=50) # lots of params are supposed to go in here
    gt.timed_train()
