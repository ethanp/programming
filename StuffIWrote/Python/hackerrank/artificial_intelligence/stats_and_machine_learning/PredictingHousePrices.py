sample_input = [
    '2 7',
    '0.18 0.89 109.85',
    '1.0 0.26 155.72',
    '0.92 0.11 137.66',
    '0.07 0.37 76.17',
    '0.85 0.16 139.75',
    '0.99 0.41 162.6',
    '0.87 0.47 151.77',
    '4',
    '0.49 0.18',
    '0.57 0.83',
    '0.56 0.64',
    '0.76 0.18'
]

in_idx = -1
run_local = True
def n():
    if run_local:
        global in_idx
        in_idx += 1
        return sample_input[in_idx]
    else:
        return raw_input()

from sklearn import linear_model

## parse input ##
F, N = map(int, n().split())
allTrain = [map(float, n().split()) for _ in xrange(N)]
T = int(n())
testData = [map(float, n().split()) for _ in xrange(T)]
trainData = [a[:-1] for a in allTrain]
yVec = [a[-1] for a in allTrain]

## build and use linear model ##
clf = linear_model.LinearRegression()
clf.fit(trainData, yVec)
#print 'coef', clf.coef_
predictions = clf.predict(testData)
for i in predictions: print i
