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

#sds = SequenceClassificationDataSet(3, 1)

sds = SequenceClassificationDataSet(3, 2)
blank_output = [1./sds.outdim]*sds.outdim
print blank_output
grammatical_label = (0, 1)
ungrammatical_label = (1, 0)

# make it learn
#     NOUN --> VERB ==> OK!
#     ADJ  --> ADJ  ==> NOPE!
# input_vector := [NOUN, VERB, ADJ]

def insert_sequence_orig(the_sentence, grammatical):
    '''
    for grammatical sentences:
        label all intermediate steps as UNGRAMMATICAL

    this has the issue where if we have

        1. he went
        2. he went home
        3. he went to work

    it will end up learning that he went has P(grammatical) = 1/3, which is incorrect
    '''
    sds.newSequence()
    for i, word_vector in enumerate(the_sentence):
        if i < len(the_sentence)-1 or not grammatical:
            sds.appendLinked(word_vector, [0])

        else:
            sds.appendLinked(word_vector, [1])

def insert_sequence_mod_1(the_sentence, grammatical):
    '''
    for grammatical sentences:
    add one version where all intermediate steps are labeled [1 0] and another labeled [0 1]
    so that on average the intermediate steps are labeled [0.5 0.5]
    '''
    sds.newSequence()
    if grammatical:
        for word_vector in the_sentence:
            sds.appendLinked(word_vector,[1])
        sds.newSequence()
    for i, word_vector in enumerate(the_sentence):
        if i < len(the_sentence)-1 or not grammatical:
            sds.appendLinked(word_vector, [0])

        else:
            sds.appendLinked(word_vector, [1])

def insert_sequence_mod_2(the_sentence, grammatical):
    sds.newSequence()
    for i, word_vector in enumerate(the_sentence):
        if grammatical:
            if i < len(the_sentence)-1:
                sds.appendLinked(word_vector, blank_output)

            else:
                sds.appendLinked(word_vector, grammatical_label)
        else:
            sds.appendLinked(word_vector, ungrammatical_label)

# the only way it could learn this is if it could see that he_went is a subset of he_went_blue
#   and not end up trying to learn that he_went is grammatical the first time, and ungrammatical the second

# it seems like one solution is to LOOK
#   for subsets in longer examples and make sure they don't create confusions
# this would call for a rather sloppy-looking change to insert_sequence
#   that's all I've got right now, so pending a better idea # TODO try that idea out!
# I guess one uses a trie data structure to pull that off efficiently

he_went = [[1, 0, 0], [0, 1, 0]]
blue_green = [[0, 0, 1], [0, 0, 1]]
he_went_blue = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]
happy_go = [[0, 1, 0], [0, 0, 1]]

sentences = [he_went, blue_green, he_went_blue, happy_go]

insert_sequence_mod_2(he_went, grammatical=True)
insert_sequence_mod_2(blue_green, grammatical=False)
insert_sequence_mod_2(he_went_blue, grammatical=True)
insert_sequence_mod_2(happy_go, grammatical=False)

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
recursive_network = buildNetwork(3, 3, 2, hiddenclass=LSTMLayer, outclass=TanhLayer, recurrent=True)


# does this help, and why?
recCon = FullConnection(recursive_network['out'], recursive_network['hidden0'])
recursive_network.addRecurrentConnection(recCon)

# must re-sort after adding another connection
recursive_network.sortModules()

print "------Before Training:"

def test_on_sentence(the_sentence):
    recursive_network.reset()
    for i, word in enumerate(the_sentence):
        if i < len(the_sentence)-1:
            recursive_network.activate(word)
        else:
            print recursive_network.activate(word)

trainer = BackpropTrainer(recursive_network, sds, verbose=True)
trainer.trainEpochs(5000)

print "------After Training:"

for a_sentence in sentences:
    test_on_sentence(a_sentence)

print recursive_network['in']
print recursive_network['hidden0']
print recursive_network['out']
