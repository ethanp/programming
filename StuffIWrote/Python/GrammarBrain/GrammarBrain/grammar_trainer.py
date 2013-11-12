''' -- EXPERIMENTS FOR HOW TO MAKE THE GRAMMATICALITY CLASSIFIER -- '''

''' -- GLOBALS --'''
MAX_LEN = 5  # maximum length of an input sentence

''' -- DATASET TYPE -- '''
# http://pybrain.org/docs/tutorial/datasets.html
# SequenceClassificationDataSet combines
#       ClassificationDataSet
#               with
#       SequentialDataSet
#
# http://pybrain.org/docs/api/datasets/classificationdataset.html
from pybrain.datasets.classification import SequenceClassificationDataSet

# inp: dimensionality of the input (I think this is the sentence length)
# number of targets (output dimensionality, I think)
# nb_classes: number of possible classifications (i.e. num of parts of speech)
        # TODO figure out what the number should be
inputData = SequenceClassificationDataSet(inp=MAX_LEN, nb_classes=5)

''' -- BPTT TRAINING ALGORITHM -- '''
# http://pybrain.org/docs/api/supervised/trainers.html
# backprop's "through time" on a sequential dataset
from pybrain.supervised.trainers import BackpropTrainer


''' -- CONSTRUCT THE NETWORK (SRN) -- '''
from pybrain.structure import RecurrentNetwork
        # TODO checkout the BidirectionalNetwork
n = RecurrentNetwork()

from pybrain.structure import LinearLayer, SigmoidLayer
n.addInputModule(LinearLayer(MAX_LEN, name='input sentence'))
n.addModule(SigmoidLayer(3, name='simple recursive hidden layer'))
n.addOutputModule(LinearLayer(1, name='bool isGrammatical layer'))

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

n.sortModules()  # does some initialization

print n.activate((1,2,1,2,1))

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
