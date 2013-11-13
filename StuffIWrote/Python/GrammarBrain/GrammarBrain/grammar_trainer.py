''' -- FIRST TRIAL OF GRAMMATICALITY CLASSIFIER (SRN w/ BPTT) -- '''
from random import sample, random
from pybrain.utilities import percentError

''' -- GLOBALS --'''
# length restrictions on input sentences
MAX_LEN = 15
MIN_LEN = 4

# number of different part of speech categorizations
NUM_REG_POS = 12
NUM_BROWN_POS = 470

HIDDEN_LAYER_SIZE = 5


''' -- THE METHODS -- '''
def create_dataset(MIN_LEN, MAX_LEN):
    # http://pybrain.org/docs/tutorial/datasets.html
    # http://pybrain.org/docs/api/datasets/classificationdataset.html
    from pybrain.datasets.classification import SequenceClassificationDataSet

    # inp: dimensionality of the input (I think this is the sentence length)
    # number of targets (output dimensionality, I think? Maybe not...I'm not sure!)
    # nb_classes: number of possible classifications (i.e. grammatical or not)
    train_data = SequenceClassificationDataSet(inp=MAX_LEN, target=1, nb_classes=2)
    test_data = SequenceClassificationDataSet(inp=MAX_LEN, target=1, nb_classes=2)

    # use nltk's BROWN dataset
    print 'vectorizing sentences'
    from get_brown_pos_sents import vectorize_sents, get_brown_tagged_sents
    vectorized_sentences = vectorize_sents(get_brown_tagged_sents(MAX_LEN, MIN_LEN),MAX_LEN)
    print 'num sentences', len(vectorized_sentences)

    print 'creating training and test sets'
    for sentence_vector in vectorized_sentences:
        if random() < .25:  # percent distribution between sets needn't be perfect, right?
            test_data.addSample(sentence_vector, [0])
        else:
            train_data.addSample(sentence_vector, [0])

    # add a negative example for good measure
    train_data.addSample([7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0], [1])
    test_data.addSample([6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0], [1])

    # encode classes with one output neuron per class
    # duplicates the original target and stores them in an integer field named 'class'
    # http://pybrain.org/docs/tutorial/fnn.html
    test_data._convertToOneOfMany()
    train_data._convertToOneOfMany()

    print_data_data(train_data, 'training')
    print_data_data(test_data, 'testing')

    return train_data, test_data


def print_data_data(data, name):
    print "num", name, "patterns: ", len(data)
    print "input and output dimensions: ", data.indim, data.outdim
    print "First sample (input, target, class):"
    print data['input'][0], data['target'][0], data['class'][0]


def build_it():
    ''' -- CONSTRUCT THE NETWORK (SRN) -- '''
    from pybrain.structure import SigmoidLayer
    # TODO checkout the SharedFullConnection, LSTMLayer, BidirectionalNetwork, etc.

    from pybrain.tools.shortcuts import buildNetwork
    network = buildNetwork(MAX_LEN, HIDDEN_LAYER_SIZE, 2,
                     hiddenclass=SigmoidLayer, outclass=SigmoidLayer,
                     recurrent=True)

    network.randomize()
    return network


# http://pybrain.org/docs/api/supervised/trainers.html
# backprop's "through time" on a sequential dataset
def train(network_module, training_data, testing_data, n=20):
    ''' -- BPTT TRAINING ALGORITHM -- '''
    from pybrain.supervised.trainers import BackpropTrainer
    trainer = BackpropTrainer(module=network_module, dataset=training_data)

    for i in range(n):
        trainer.trainEpochs(epochs=1)
        print 'epoch', i, 'finished'

        # http://pybrain.org/docs/tutorial/fnn.html
        train_result = percentError(
            trainer.testOnClassData(),
            train_data['class'])

        test_result = percentError(
            trainer.testOnClassData(dataset=testing_data),
            test_data['class'])

        print "epoch: %4d" % trainer.totalepochs, \
            "  train error: %5.2f%%" % train_result, \
            "  test error: %5.2f%%" % test_result


train_data, test_data = create_dataset(MIN_LEN, MAX_LEN)

network = build_it()

train(network_module=network, training_data=train_data, testing_data=test_data, n=10)
