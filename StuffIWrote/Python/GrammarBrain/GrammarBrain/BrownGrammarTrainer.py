from random import random, shuffle
import time
import csv

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
from brown_data.experiment_scripts import EXPERIMENT_RESULT_PATH


GRAMMATICAL = (0, 1)
UNGRAMMATICAL = (1, 0)
MID_SENTENCE = (0.5, 0.5)

'''
    How Experiment Printing Works:
    ==============================

 1. load some data-structure (dict?) with the hyperparameters

 2. as the thing is training, be concatenating some list with
    the error-numbers over the training, (validation?,) and test sets

 3. print the param_dict as `comma_separated(keys())\ncomma_separated(values())`

 4. print the training as

    epoch,    training error,   test error
     1,          .4,              .5
     2,          .35,             .49
     ...,        ...,             ...

'''
class BrownGrammarTrainer(object):
    #noinspection PyTypeChecker
    def __init__(self, title='default title', minim=4, maxim=5, outdim=2, hiddendim=None,
                 train_time=50, basic_pos=True, hidden_type=LSTMLayer,
                 output_type=TanhLayer):
        if not hiddendim: hiddendim = [5]
        self.TITLE       = title
        self.MIN_LEN     = minim
        self.MAX_LEN     = maxim
        self.basic_pos   = basic_pos
        self.NUM_POS     = len(pos_vector_mapping) if basic_pos else len(pos_reducer)
        self.HIDDEN_LIST = hiddendim
        self.HIDDEN_TYPE = hidden_type
        self.NUM_OUTPUTS = outdim
        self.OUTPUT_TYPE = output_type
        self.network     = self.build_network()
        self.training_iterations = train_time
        print str(self)
        self.train_set, self.test_set, self.val_set = self.create_TrnTstVal_sets()
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


    def create_TrnTstVal_sets(self):

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
        train_data = SequenceClassificationDataSet(inp=self.NUM_POS, target=self.NUM_OUTPUTS)
        test_data = SequenceClassificationDataSet(inp=self.NUM_POS, target=self.NUM_OUTPUTS)
        validation_data = SequenceClassificationDataSet(inp=self.NUM_POS, target=self.NUM_OUTPUTS)

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
            r = random()
            if r < .15:  # percent distribution between sets needn't be perfect, right?
                insert_grammatical_sequence(validation_data, sentence_matrix)
                insert_randomized_sequence(validation_data, sentence_matrix)
            elif r < .30:
                insert_grammatical_sequence(test_data, sentence_matrix)
                insert_randomized_sequence(test_data, sentence_matrix)
            else:
                insert_grammatical_sequence(train_data, sentence_matrix)
                insert_randomized_sequence(train_data, sentence_matrix)

        ''' FOR DEBUGGING DATASET '''
        #print_data_data(train_data, 'training')
        #print_data_data(test_data, 'testing')

        return train_data, test_data, validation_data


    def build_network(self):
        network_options = {
            'hiddenclass'   : self.HIDDEN_TYPE,
            'outclass'      : self.OUTPUT_TYPE,
            'recurrent'     : True,
            'bias'          : True
        }
        layout = tuple([self.NUM_POS] + self.HIDDEN_LIST + [self.NUM_OUTPUTS])
        network = buildNetwork(*layout, **network_options)

        # these are the default "module" names
        # NOTE: you DO have to add a hidden->hidden connection even when you set rec=True
        #   bc otw how would it know that you wanted that /particular/ connection!?
        h = network['hidden0']
        o = network['out']
        #network.addRecurrentConnection(FullConnection(h, h)) made automatically below?
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
            training_error = 1 - testOnSequenceData(network_module, training_data)
            print '\nTRAINING error: {:.3f}'.format(training_error)

            test_error = 1 - testOnSequenceData(network_module, testing_data)
            print 'TEST error: {:.3f}\n'.format(test_error)

            self.train_dict[(i+1)*s] = {
                'train' : training_error,
                'test'  : test_error
            }


    def timed_train(self, s=1):
        start = time.clock()

        self.train(network_module=self.network,
                   training_data=self.train_set,
                   testing_data=self.test_set,
                   n=self.training_iterations,
                   s=s)

        train_minutes = (time.clock() - start) / 60
        print 'Total Train Time: %.2f minutes' % train_minutes
        self.repr_dict['train time'] = train_minutes


    def make_csv(self):
        repr_list = [
            ('title'                 , self.TITLE),
            ('min len'               , self.MIN_LEN),
            ('max len'               , self.MAX_LEN),
            ('num pos'               , self.NUM_POS),
            ('hidden list'           , self.HIDDEN_LIST),
            ('hidden type'           , 'LSTM' if self.HIDDEN_TYPE == LSTMLayer else 'Other'),
            ('output type'           , 'Tanh' if self.OUTPUT_TYPE == TanhLayer else 'Other'),
            ('training iterations'   , self.training_iterations),
            ('train set size'        , self.train_set.getNumSequences()),
            ('test set size'         , self.test_set.getNumSequences())
        ]
        csv_filename = EXPERIMENT_RESULT_PATH + self.TITLE + '_' + time.strftime("%m:%d-%H:%M") + '.csv'
        with open(csv_filename, 'wb') as csv_file:
            writer = csv.writer(csv_file)
            writer.writerow(t for t, v in repr_list)
            writer.writerow(v for t, v in repr_list)
            writer.writerow(('Epoch', 'Train Error', 'Test Error'))
            for logged_epoch in self.train_dict.keys():
                writer.writerow((
                    logged_epoch,
                    self.train_dict[logged_epoch]['train'],
                    self.train_dict[logged_epoch]['test']
                ))

            # TODO write the validation error


if __name__ == "__main__":
    gt = BrownGrammarTrainer(hiddendim=50) # lots of params are supposed to go in here
    gt.timed_train()
