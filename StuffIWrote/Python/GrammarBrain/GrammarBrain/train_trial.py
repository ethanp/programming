# This is a test file
# it is based on
# https://github.com/kyle4211/cs165b/blob/fe02a19d54f92af797c0163b20679af93b7f5804/testpar.py
# but more related to my project, with extra help from
# pybrain/examples/neuralnets+svm/example_rnn.py

from pybrain.tools.shortcuts import buildNetwork
from pybrain.datasets.classification import SequenceClassificationDataSet
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.structure.connections import FullConnection
from pybrain.structure.modules import TanhLayer, LSTMLayer

# Load Dataset
# specifying nb_classes is unnecessary, it'll figure out that it's 2 from the data

sds = SequenceClassificationDataSet(3, 1)

# TODO make this echo my dataset, by having one bit on in each vector at a time
# e.g. make it learn
#               NOUN --> VERB ==> OK!
#               ADJ  --> ADJ  ==> NOPE!

sds.appendLinked([0,0,0], [0])
sds.appendLinked([0,0,0], [0])

sds.newSequence()
sds.appendLinked([0,0,0], [0])
sds.appendLinked([1,0,0], [0])

sds.newSequence()
sds.appendLinked([1,1,1], [0])
sds.appendLinked([1,1,1], [1])

sds.newSequence()
sds.appendLinked([0,0,0], [0])
sds.appendLinked([1,1,1], [1])

print sds['input'] # array of all n inputs (note: here, n=8, not 4)
print sds['target'] # array of all n targets (1 by n array)

# makes it so there are the same number of output neurons as classes
sds._convertToOneOfMany()

print 'converted:'
print sds['target'] # now it's a (2 by n array)

# Build a recurrent Network.

# bias and outputbias are on by default. I can't think of any good reason to turn them off
# I suppose it's situation-specific, and in my situation, I just want to learn the function
#   without any constraints on what that crazy function's intercept values are
# bias adds a "biasModule" on all the hidden layers
#   and if outputbias is True too, then also on the output layer
rnet = buildNetwork(3, 3, 2, hiddenclass=LSTMLayer, outclass=TanhLayer, recurrent=True)


# does this help, and why?
recCon = FullConnection(rnet['out'], rnet['hidden0'])
rnet.addRecurrentConnection(recCon)

# must re-sort after adding another connection
rnet.sortModules()

print "------Before Training:"

rnet.activate([0,0,0])
print rnet.activate([0,0,0])
rnet.reset()

rnet.activate([0,0,0])
print rnet.activate([1,0,0])
rnet.reset()

rnet.activate([1,1,1])
print rnet.activate([1,1,1])
rnet.reset()

rnet.activate([0,0,0])
print rnet.activate([1,1,1])
rnet.reset()

print rnet['in']
print rnet['hidden0']
print rnet['out']

print

trainer = BackpropTrainer(rnet, sds, verbose=True)
trainer.trainEpochs(5000)

print "------After Training:"

rnet.activate([0,0,0])
print rnet.activate([0,0,0])
rnet.reset()

rnet.activate([0,0,0])
print rnet.activate([1,0,0])
rnet.reset()

rnet.activate([1,1,1])
print rnet.activate([1,1,1])
rnet.reset()

rnet.activate([0,0,0])
print rnet.activate([1,1,1])
rnet.reset()

print rnet['in']
print rnet['hidden0']
print rnet['out']
