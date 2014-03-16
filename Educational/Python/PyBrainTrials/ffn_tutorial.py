''' -- CLASSIFICATION WITH FEEDFORWARD NETWORKS -- '''
# http://pybrain.org/docs/tutorial/fnn.html

# note: this is pretty-much awesome

from pybrain.datasets            import ClassificationDataSet
from pybrain.utilities           import percentError
from pybrain.tools.shortcuts     import buildNetwork
from pybrain.supervised.trainers import BackpropTrainer
from pybrain.structure.modules   import SoftmaxLayer

# plotting from pylab
from pylab import ion, ioff, figure, draw, contourf, clf, show, hold, plot
from scipy import diag, arange, meshgrid, where
from numpy.random import multivariate_normal

# define three classes of points in 2D
# each has its respective mean and covariance
means = [(-1,0),(2,4),(3,1)]
cov = [diag([1,1]), diag([0.5,1.2]), diag([1.5,0.7])]

# declare a dataset with 2D input, and 1D output with 3 classification-options
alldata = ClassificationDataSet(inp=2, target=1, nb_classes=3)

# add 400 points in each classification,
# sampling point from multivariate_normal dist
for n in xrange(400):
    for klass in range(3):
        input = multivariate_normal(means[klass],cov[klass])
        alldata.addSample(input, [klass])

# split data: 75% for training, 25% for testing
tstdata, trndata = alldata.splitWithProportion( 0.25 )

# encode classes as one output unit per class
# used to make "many algorithms work better"
# ClassificationDataSet does it automatically
# http://pybrain.org/docs/tutorial/datasets.html
# old target becomes field "class"
trndata._convertToOneOfMany()
tstdata._convertToOneOfMany()

print "Number of training patterns: ", len(trndata)
print "Input and output dimensions: ", trndata.indim, trndata.outdim
print "First sample (input, target, class):"
print trndata['input'][0], trndata['target'][0], trndata['class'][0]

# Shortcut to build a ffn with 5 hidden units
# "You could add additional hidden layers by inserting
#  more numbers giving the desired layer sizes."
fnn = buildNetwork(trndata.indim, 5, trndata.outdim, outclass=SoftmaxLayer)

# there are more training algorithms in 'trainers'
trainer = BackpropTrainer(fnn,
                          dataset=trndata,
                          momentum=0.1,
                          verbose=True,
                          weightdecay=0.01)

# generate square plotting area
ticks = arange(-3.,6.,0.2)     # array([-3.0,-2.8,-2.6,...,5.8])
X, Y = meshgrid(ticks, ticks)  # wireframe with wires from array vals for both axes

# "need column vectors in dataset, not arrays"
griddata = ClassificationDataSet(2,1, nb_classes=3)
for i in xrange(X.size):

    # ravel(array([1,2,3],[4,5,6])) => [1 2 3 4 5 6]
    # ravel(array([1,2,3],[4,5,6]), order='F') => [1 4 2 5 3 6] (like zipping)

    # ([X.ravel()[i],Y.ravel()[i]], [0]) =>
    #   ([-3.0, -3.0], [0])
    #   ([-2.8, -3.0], [0])
    #   ...
    #   ([5.8, -3.0], [0])
    #   ([-3.0, -2.8], [0])
    #   ...
    #   ([5.8, 5.8], [0])
    griddata.addSample([X.ravel()[i],Y.ravel()[i]], [0])  # fctn is optimized for speed
griddata._convertToOneOfMany()  # fnn asks for this even though we aren't using it

# train for 20 epochs
for i in range(4):
    trainer.trainEpochs(5)
    trnresult = percentError(trainer.testOnClassData(),trndata['class'])
    tstresult = percentError(trainer.testOnClassData(dataset=tstdata),tstdata['class'])

    print "epoch: %4d" % trainer.totalepochs, \
          "  train error: %5.2f%%" % trnresult, \
          "  test error: %5.2f%%" % tstresult

# the chart created here always freezes...
out = fnn.activateOnDataset(griddata)
out = out.argmax(axis=1)  # the highest output activation gives the class
out = out.reshape(X.shape)
figure(1)
ioff()  # interactive graphics off
clf()   # clear the plot
hold(True) # overplot on
for c in [0,1,2]:
    here, _ = where(tstdata['class']==c)
    plot(tstdata['input'][here,0],tstdata['input'][here,1],'o')
if out.max()!=out.min():  # safety check against flat field
    contourf(X, Y, out)   # plot the contour
ion()   # interactive graphics on
draw()  # update the plot
ioff()
show()
