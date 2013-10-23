import csv
import multiprocessing
import nltk
import time
import Project
import numpy as np
BUSINESS = 0
CHECKIN = 1
REVIEW = 2
USER = 3

def writeTestSetCSV(predictionTuples):
    print 'writing tests to csv'
    with open('adjPredictions.csv','wb') as f:
        writer = csv.writer(f)
        writer.writerow(['id','votes'])
        writer.writerows(predictionTuples)


#----------------------------------------------------------------------------------------------
def testConcAdjAdv(testFiles, weights):
    """
     predict scores in the test set using the number of lines, adjectives, and adverbs (takes a long time)
    """
    print 'import test reviews'
    reviewsList = Project.importJSONFiles([testFiles[REVIEW]])[0]

    LENGTH = 100
    PROCESSES = 8
    # lenPerProc = LENGTH / PROCESSES + 1
    lenPerProc = len(reviewsList) / PROCESSES + 1
    print 'Tokenizing', LENGTH, 'reviews using', PROCESSES, 'processes...'
    print 'Tokenizing', len(reviewsList), 'reviews using', PROCESSES, 'processes...'
    start = time.time()

    pool = multiprocessing.Pool(processes=PROCESSES,
                                initializer=Project.start_process,
                                maxtasksperchild=lenPerProc)

    # - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    # partial (for trying it out)
    # pool_outputs = pool.map(Project.pooledAdjAdv, reviewsList[:LENGTH])

    # whole thing (for preparing for submission)
    pool_outputs = pool.map(Project.pooledAdjAdv, reviewsList)
    # - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    pool.close()
    pool.join()
    end = time.time()
    print 'Reviews finished tokenizing, it took %.2f seconds' % (end - start)

    testMatrix = np.asmatrix(pool_outputs)
    predictedScores = predictScores(testMatrix, weights)
    predictedList = np.array(predictedScores).flatten().tolist()

    # - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    # partial
    # predictionTuples = zip([review['review_id'] for review in reviewsList[:LENGTH]], predictedList)

    # whole thing
    predictionTuples = zip([review['review_id'] for review in reviewsList], predictedList)
    # - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    writeTestSetCSV(predictionTuples)

#----------------------------------------------------------------------------------------------
def testLinesSentences(testFiles, weights):
    """
     predict the score of the test set using the number of lines and sentences
    """
    print 'importing test reviews'
    reviewsList = Project.importJSONFiles([testFiles[REVIEW]])[0]

    print 'tokenizing test reviews'
    testMatrix = np.asmatrix([(1,                                                # column of 1s
                                len(review['text'].splitlines()),                 # column of numParagraphs
                                len(nltk.tokenize.sent_tokenize(review['text']))) # column of numSentences
                               for review in reviewsList])                              # 1 row per review

    predictedScores = predictScores(testMatrix, weights)

    predictedList = np.array(predictedScores).flatten().tolist()

    predictionTuples = zip([review['review_id'] for review in reviewsList], predictedList)

    writeTestSetCSV(predictionTuples)

#----------------------------------------------------------------------------------------------
# DOESN'T WORK because this data is only given in the training set!
def testUserScores(testFiles, weights):
    """
     predict the score of the test set using corresponding user average scores
    """
    neededFiles = [testFiles[REVIEW], testFiles[USER]]
    print 'importing test reviews and users'
    reviewsList, usersList = Project.importJSONFiles(neededFiles)
    print 'building dictionary of test users'
    userAvgs = {}
    for user in usersList:
        userID = user['user_id']
        userAvgs[userID] = {}
        thisUser = userAvgs[userID]
        thisUser['useful'] = user['votes']['useful'] / float(user['review_count'])
        thisUser['funny']  = user['votes']['funny']  / float(user['review_count'])
        thisUser['cool']   = user['votes']['cool']   / float(user['review_count'])

    # Dictionaries raise a "KeyError" if you try to access a key not in them
    defaultDict = {'useful': 0, 'funny': 0, 'cool': 0}

    print 'building matrix'
    testList = []
    for review in reviewsList:
        thisUser = userAvgs.get(review['user_id'], defaultDict)
        testList += [(1, thisUser['useful'], thisUser['funny'], thisUser['cool'])]

    testMatrix = np.asmatrix(testList)

    predictedScores = predictScores(testMatrix, weights)

    # print them out
    # writeTestSetCSV()


def predictScores(X,W): return X * W
