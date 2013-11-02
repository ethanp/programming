''' -- EXPERIMENTS FOR HOW TO MAKE THE GRAMMATICALITY CLASSIFIER -- '''
# started 10/28/13

''' -- DATASET TYPE -- '''
# http://pybrain.org/docs/tutorial/datasets.html
# SequenceClassificationDataSet combines
#       ClassificationDataSet
#               with
#       SequencialDataSet
from pybrain.datasets.classification import SequenceClassificationDataSet

inp = SequenceClassificationDataSet()

''' -- BPTT TRAINING ALGORITHM -- '''
# http://pybrain.org/docs/api/supervised/trainers.html
# backprop's "through time" on a sequencial dataset
from pybrain.supervised.trainers import BackpropTrainer


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
