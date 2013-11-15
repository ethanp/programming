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

# make it learn
#     NOUN --> VERB ==> OK!
#     ADJ  --> ADJ  ==> NOPE!
# input_vector := [NOUN, VERB, ADJ]

def insert_sequence(the_sentence, grammatical):
    sds.newSequence()
    for i, word_vector in enumerate(the_sentence):

        if i < len(the_sentence)-1 or not grammatical:
            sds.appendLinked(word_vector, [0])

        else:
            sds.appendLinked(word_vector, [1])

# the only way it could learn this is if it could see that he_went is a subset of he_went_blue
#   and not end up trying to learn that he_went is grammatical the first time, and ungrammatical the second

# it seems like the only solution is to LOOK
#   for subsets in longer examples and make sure they don't create confusions
# this would call for a rather sloppy-looking change to insert_sequence
#   that's all I've got right now, so pending a better idea # TODO try that idea out!

# Another Idea
# label mid-sentences as [0.5, 0.5]
# i.e they give zero information
# they will have the effect of dampening the learning of smaller sentences
# but that may be tolerable

he_went = [[1, 0, 0], [0, 1, 0]]
blue_green = [[0, 0, 1], [0, 0, 1]]
he_went_blue = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]

sentences = [he_went, blue_green, he_went_blue]

insert_sequence(he_went, grammatical=True)
insert_sequence(blue_green, grammatical=False)
insert_sequence(he_went_blue, grammatical=True)

# "happy go"
insert_sequence([[0,1,0],[0,0,1]], grammatical=False)

print sds['input']  # array of all n inputs (note: here, n=8, not 4)
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

def test_on_sentence(the_sentence):
    rnet.reset()
    for i, word in enumerate(the_sentence):
        if i < len(the_sentence)-1:
            rnet.activate(word)
        else:
            print rnet.activate(word)

trainer = BackpropTrainer(rnet, sds, verbose=True)
trainer.trainEpochs(5000)

print "------After Training:"

for a_sentence in sentences:
    test_on_sentence(a_sentence)

print rnet['in']
print rnet['hidden0']
print rnet['out']
