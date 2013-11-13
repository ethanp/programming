''' -- EXPERIMENTS FOR HOW TO MAKE THE GRAMMATICALITY CLASSIFIER -- '''
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

''' -- CREATE DATASET -- '''
# http://pybrain.org/docs/tutorial/datasets.html
# SequenceClassificationDataSet combines
#       ClassificationDataSet
#               with
#       SequentialDataSet
#
# http://pybrain.org/docs/api/datasets/classificationdataset.html
from pybrain.datasets.classification import SequenceClassificationDataSet

# inp: dimensionality of the input (I think this is the sentence length)
# number of targets (output dimensionality, I think? Maybe not...I'm not sure!)
# nb_classes: number of possible classifications (i.e. grammatical or not)
all_data = SequenceClassificationDataSet(inp=MAX_LEN, target=1, nb_classes=2)

train_data = SequenceClassificationDataSet(inp=MAX_LEN, target=1, nb_classes=2)
test_data = SequenceClassificationDataSet(inp=MAX_LEN, target=1, nb_classes=2)

# use nltk's BROWN dataset
print 'vectorizing sentences'
from get_brown_pos_sents import vectorize_sents, get_brown_tagged_sents
vectorized_sentences = vectorize_sents(get_brown_tagged_sents(MAX_LEN, MIN_LEN),MAX_LEN)

print 'creating training and test sets'
for sentence_vector in vectorized_sentences:
    if random() < .25:  # percent distribution between sets needn't be perfect, right?
        test_data.addSample(sentence_vector, [0])
    else:
        train_data.addSample(sentence_vector, [0])

# add a negative example for good measure
train_data.addSample([7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 0], [1])
test_data.addSample([6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 0], [1])

print 'num sentences', len(vectorized_sentences)
l = all_data.getLength()
print 'num seqs', l
leftIndices = sample(range(l), int(l * 0.5))
print leftIndices[:3]


# encode classes with one output neuron per class
# duplicates the original target and stores them in an integer field named 'class'
# http://pybrain.org/docs/tutorial/fnn.html
test_data._convertToOneOfMany()
train_data._convertToOneOfMany()

print "num train patterns: ", len(train_data)
print "input and output dimensions: ", train_data.indim, train_data.outdim
print "First sample (input, target, class):"
print train_data['input'][0], train_data['target'][0], train_data['class'][0]

print "num test patterns: ", len(test_data)
print "input and output dimensions: ", test_data.indim, test_data.outdim
print "First sample (input, target, class):"
print test_data['input'][0], test_data['target'][0], test_data['class'][0]

''' -- CONSTRUCT THE NETWORK (SRN) -- '''
from pybrain.structure import RecurrentNetwork
        # TODO checkout the BidirectionalNetwork
n = RecurrentNetwork()

from pybrain.structure import LinearLayer, SigmoidLayer

# TODO there's probably a `buildNetwork()` shortcut for all this stuff
n.addInputModule(
    LinearLayer(
        train_data.indim,
        name='input sentence'))

n.addModule(
    SigmoidLayer(
        HIDDEN_LAYER_SIZE,
        name='simple recursive hidden layer'))

n.addOutputModule(
    LinearLayer(
        train_data.outdim,
        name='bool isGrammatical layer'))

from pybrain.structure import FullConnection
        # TODO checkout the SharedFullConnection, LSTMLayer, etc.
n.addConnection(
    FullConnection(
        n['input sentence'],
        n['simple recursive hidden layer'],
        name='in to hidden'))

n.addConnection(
    FullConnection(
        n['simple recursive hidden layer'],
        n['bool isGrammatical layer'],
        name='hidden to out'))

n.addRecurrentConnection(
    FullConnection(
        n['simple recursive hidden layer'],
        n['simple recursive hidden layer'],
        name='recursive hidden connection'))

n.sortModules()  # initialize the network


''' -- BPTT TRAINING ALGORITHM -- '''
# http://pybrain.org/docs/api/supervised/trainers.html
# backprop's "through time" on a sequential dataset
from pybrain.supervised.trainers import BackpropTrainer
trainer = BackpropTrainer(
    module=n,
    dataset=train_data,
    momentum=0.1,
    weightdecay=0.1)

for i in range(20):
    trainer.trainEpochs(epochs=1)
    print 'epoch', i, 'finished'

    # http://pybrain.org/docs/tutorial/fnn.html
    train_result = percentError(
        trainer.testOnClassData(),
        train_data['class'])

    test_result = percentError(
        trainer.testOnClassData(dataset=test_data),
        test_data['class'])

    print "epoch: %4d" % trainer.totalepochs, \
        "  train error: %5.2f%%" % train_result, \
        "  test error: %5.2f%%" % test_result
# NOTE: n.reset() will clear the history of the network

###########################################################################################
''' -- SAVING AND RELOADING TRAINED PYBRAINS -- '''
# http://stackoverflow.com/questions/6006187/how-to-save-and-recover-pybrain-traning/6009051
# "PyBrain's Neural Networks can be saved and loaded using either
#     python's built in pickle/cPickle module,
#            or
#     by using PyBrain's XML NetworkWriter."
'''
# Using pickle

from pybrain.tools.shortcuts import buildNetwork
import pickle

net = buildNetwork(2,4,1)

fileObject = open('filename', 'w')

pickle.dump(net, fileObject)

fileObject.close()

fileObject = open('filename','r')
net = pickle.load(fileObject)

Note cPickle is implemented in C, and therefore should be much faster than pickle.
Usage should mostly be the same as pickle, so just import and use cPickle instead.

# Using NetworkWriter

from pybrain.tools.shortcuts import buildNetwork
from pybrain.tools.xml.networkwriter import NetworkWriter
from pybrain.tools.xml.networkreader import NetworkReader

net = buildNetwork(2,4,1)

NetworkWriter.writeToFile(net, 'filename.xml')
net = NetworkReader.readFrom('filename.xml')
'''

''' -- PLAIN OF ACTION --
1. get a dataset
2. train SRN on length 3 sentences
3. make a training set
4. see how it performs on it
'''
