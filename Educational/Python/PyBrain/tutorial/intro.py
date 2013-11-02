''' -- FEEDFORWARD NETWORK -- '''
# http://pybrain.org/docs/tutorial/intro.html

from pybrain.structure import FeedForwardNetwork
n = FeedForwardNetwork()

from pybrain.structure import LinearLayer, SigmoidLayer
inLayer = LinearLayer(2, name='a')
hiddenLayer = SigmoidLayer(3, name='b')
outLayer = LinearLayer(1, name='c')

n.addInputModule(inLayer)
n.addModule(hiddenLayer)
n.addOutputModule(outLayer)

from pybrain.structure import FullConnection
in_to_hidden = FullConnection(inLayer, hiddenLayer)
hidden_to_out = FullConnection(hiddenLayer, outLayer)

n.addConnection(in_to_hidden)
n.addConnection(hidden_to_out)

# does necessary initialization, sorts "topologically"
n.sortModules()

# print structure of neural net
#print n

# get output for presentation of input
#print n.activate([1,2])

# print weights
#print in_to_hidden.params

''' -- RECURRENT NETWORK -- '''
from pybrain.structure import RecurrentNetwork
n = RecurrentNetwork()

n.addInputModule(LinearLayer(2, name='in'))
n.addModule(SigmoidLayer(3, name='hidden'))
n.addOutputModule(LinearLayer(1, name='out'))
n.addConnection(FullConnection(n['in'], n['hidden'], name='c1'))
n.addConnection(FullConnection(n['hidden'], n['out'], name='c2'))

n.addRecurrentConnection(FullConnection(n['hidden'], n['hidden'], name='c3'))

n.sortModules()

# the result is different every time
#print n.activate((2,2))
#print n.activate((2,2))
#print n.activate((2,2))

# goes back to its original initialization state
n.reset()
#print n.activate((2,2))
#print n.activate((2,2))
#print n.activate((2,2))
