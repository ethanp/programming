"""
Ethan Petuchowski

Started: 4/18/13
To be finished by: 5/3/13
"""

import json
import math
import os
import numpy as np
import nltk
from nltk.tokenize import WordPunctTokenizer
import time
import NaiveBayes
import Graphs
import TestSet
import multiprocessing


def main():

    trainingFiles, testFiles = getYELPFiles()
    print 'importing ~230,000 reviews...'
    reviewsList = importJSONFiles([trainingFiles[REVIEW]])[0]
    print 'import finished'

    # construct list "y" of scores
    scoreVector = np.asmatrix([review['votes']['useful'] for review in reviewsList]).T

    # GENERATE GRAPH
    #################################
    Graphs.helpfulDist(reviewsList)
    #################################

    # CONCURRENT REGRESSION CONFIGURATIONS
    ###############################################################################################
    pid1 = os.fork()
    if pid1 == 0:
        weightVector, RMSLE = regSentences(reviewsList, scoreVector)       # RMSLE = 0.6447
        exit(RMSLE)

    pid2 = os.fork()
    if pid2 == 0:
        weightVector, RMSLE = regLines(reviewsList, scoreVector)           # RMSLE = 0.6382
        exit(RMSLE)

    pid3 = os.fork()
    if pid3 == 0:
        weightVector, RMSLE = regLinesSqrLines(reviewsList, scoreVector)   # RMSLE = 0.6371
        exit(RMSLE)

    pid4 = os.fork()
    if pid4 == 0:
        weightVector, RMSLE = regLinesLogLines(reviewsList, scoreVector)   # RMSLE = 0.6365
        exit(RMSLE)

    pid5 = os.fork()
    if pid5 == 0:
        weightVector, RMSLE = regLinesSentences(reviewsList, scoreVector)  # RMSLE = 0.6320
        exit(RMSLE)

    pid6 = os.fork()
    if pid6 == 0:
        weightVector, RMSLE = regUserScores(reviewsList, scoreVector, trainingFiles)  # RMSLE = 0.5330
        exit(RMSLE)
    weightVector, RMSLE = regUserScores(reviewsList, scoreVector, trainingFiles)  # RMSLE = 0.5330

    pid7 = os.fork()
    if pid7 == 0:
        weightVector, RMSLE = regLogLinesLogSentences(reviewsList, scoreVector)  # RMSLE = 0.6340
        exit(RMSLE)

    RMSLE1 = os.waitpid(pid1,0)
    RMSLE2 = os.waitpid(pid2,0)
    RMSLE3 = os.waitpid(pid3,0)
    RMSLE4 = os.waitpid(pid4,0)
    RMSLE5 = os.waitpid(pid5,0)
    RMSLE6 = os.waitpid(pid6,0)
    RMSLE7 = os.waitpid(pid7,0)
    ###############################################################################################



    # REGRESSION (with testing) ON ADJECTIVES AND ADVERBS  RMSLE = 0.6329
    #################################################################################
    # CONCURRENT training  (set desired number of training reviews to use inside the method)
    weightVector, RMSLE = concurrentFeatureExtractor(reviewsList, scoreVector)

    # SEQUENTIAL training  (set desired number of training reviews to use inside the method)
    weightVector, RMSLE = regLinesAdjAdv(reviewsList, scoreVector)

    # concurrent testing
    TestSet.testConcAdjAdv(testFiles, weightVector)
    #################################################################################



    # 2 other possible test configurations
    #################################################################
    weightVector, RMSLE = regLinesSentences(reviewsList, scoreVector)
    TestSet.testLinesSentences(testFiles, weightVector)
    #################################################################




    # NAIVE BAYES
    ####################################################
    NaiveBayes.probScoreGivenCategories(trainingFiles)
    ####################################################

    print '\nGot to the end, Terminating...'
    # time.sleep(2)


###########################################  The Methods  ############################################
def regUserScores(reviewsList, scoreVector, trainingFiles):
    """
     Train a linear regression on {UserAvgHelpfulness,UserAvgFunniness, UserAvgCoolness}
    """
    print 'importing ~44,000 users...'
    userList = importJSONFiles([trainingFiles[USER]])[0]

    print 'building dictionary of users'
    userAvgs = {}
    for user in userList:
        userID = user['user_id']
        userAvgs[userID] = {}
        thisUser = userAvgs[userID]
        thisUser['useful'] = user['votes']['useful'] / float(user['review_count'])
        thisUser['funny'] = user['votes']['funny'] / float(user['review_count'])
        thisUser['cool'] = user['votes']['cool'] / float(user['review_count'])

    # Dictionaries raise a "KeyError" if you try to access a key not in them
    defaultDict = {'useful': 0, 'funny': 0, 'cool': 0}

    print 'building matrix'
    trainList = []
    for review in reviewsList:
        thisUser = userAvgs.get(review['user_id'], defaultDict)
        trainList += [(1, thisUser['useful'], thisUser['funny'], thisUser['cool'])]

    trainMatrix = np.asmatrix(trainList)

    weightVector, RMSLE = doTrain(trainMatrix, scoreVector)
    print 'Training on {UserAvgHelpfulness,UserAvgFunniness, UserAvgCoolness} produced an RMSLE of', RMSLE
    return weightVector, RMSLE

# ---- This One Takes a Long Time - Reference: http://pymotw.com/2/multiprocessing/communication.html --
def concurrentFeatureExtractor(reviewsList, scoreVector):
    LENGTH = 100000  # 100k should take ~4 hours
    PROCESSES = 8
    lenPerProc = LENGTH / PROCESSES + 1
    print 'Tokenizing', LENGTH, 'reviews using', PROCESSES, 'processes...'
    start = time.time()

    pool = multiprocessing.Pool(processes=PROCESSES,
                                initializer=start_process,
                                maxtasksperchild=lenPerProc)

    pool_outputs = pool.map(pooledAdjAdv, reviewsList[:LENGTH])
    pool.close()
    pool.join()
    end = time.time()
    print 'Reviews finished tokenizing, it took %.2f seconds' % (end - start)

    trainMatrix = np.asmatrix(pool_outputs)
    weightVector, RMSLE = doTrain(trainMatrix, scoreVector[:LENGTH])

    print 'Training on {lines, adjectives, adverbs} produced an RMSLE of', RMSLE
    return weightVector, RMSLE

def start_process():
    print 'Starting', multiprocessing.current_process().name

def pooledAdjAdv(review):
    reviewText = review['text']
    length = len(reviewText.splitlines())
    numAdj = 0
    numAdv = 0
    for word in nltk.pos_tag(WordPunctTokenizer().tokenize(review['text'])):
        if word[1] == 'JJ':numAdj += 1
        if word[1] == 'RB': numAdv += 1
    return 1, length, numAdj, numAdv

def regLinesAdjAdv(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones lines adjectives adverbs ] [ weights ] = [ numVotes ]
    """

    LENGTH = 100  # 100k should take ~4 hours
    print 'Starting to tokenize', LENGTH, 'reviews'

    t0 = time.time()
    forMatrix = []
    for review in reviewsList[:LENGTH]:
        reviewText = review['text']
        length = len(reviewText.splitlines())
        numAdj = 0
        numAdv = 0
        for word in nltk.pos_tag(WordPunctTokenizer().tokenize(review['text'])):
            if word[1] == 'JJ': numAdj += 1
            if word[1] == 'RB': numAdv += 1
        forMatrix += [(1, length, numAdj, numAdv)]
    # trainMatrix = np.asmatrix(forMatrix)
    t1 = time.time()
    print LENGTH, 'reviews finished tokenizing'
    print forMatrix
    print 'took %.3f seconds' % (t1 - t0)

    trainMatrix = np.asmatrix(forMatrix)

    weightVector = findWeightsLinReg(trainMatrix,scoreVector)
    RMSLE = findRootMeanSquareLogError(trainMatrix,weightVector,scoreVector)

    print 'Training on {lines, adjectives, adverbs} produced an RMSLE of ', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regSentences(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones numSentences ] [ weights ] = [ numVotes ]
    """

    print 'Tokenizing reviews...'  # (it takes a while)

    trainMatrix = np.asmatrix([(1,                                                # column of 1s
                                len(nltk.tokenize.sent_tokenize(review['text']))) # column of numSentences
                               for review in reviewsList])                              # 1 row per review

    print 'Reviews finished tokenizing'

    weightVector = findWeightsLinReg(trainMatrix,scoreVector)
    RMSLE = findRootMeanSquareLogError(trainMatrix,weightVector,scoreVector)

    print 'Training on sentences produced an RMSLE of ', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regLogLinesLogSentences(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones log(numLines) log(numSentences) ] [ weights ] = [ numVotes ]
    """

    print 'Tokenizing reviews...'  # (it takes a while)

    trainMatrix = np.asmatrix([(1,                                                # column of 1s
                        math.log(len(review['text'].splitlines())+1),                 # column of numParagraphs
                        math.log(len(nltk.tokenize.sent_tokenize(review['text'])))+1) # column of numSentences
                       for review in reviewsList])                              # 1 row per review

    print 'Reviews finished tokenizing'

    weightVector = findWeightsLinReg(trainMatrix,scoreVector)
    RMSLE = findRootMeanSquareLogError(trainMatrix,weightVector,scoreVector)

    print 'Training on log-lines and log-sentences produced an RMSLE of ', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regLinesSentences(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones numLines numSentences ] [ weights ] = [ numVotes ]
    """

    print 'Tokenizing reviews...'  # (it takes a while)

    trainMatrix = np.asmatrix([(1,                                                # column of 1s
                                len(review['text'].splitlines()),                 # column of numParagraphs
                                len(nltk.tokenize.sent_tokenize(review['text']))) # column of numSentences
                               for review in reviewsList])                              # 1 row per review

    print 'Reviews finished tokenizing'

    weightVector = findWeightsLinReg(trainMatrix,scoreVector)
    RMSLE = findRootMeanSquareLogError(trainMatrix,weightVector,scoreVector)

    print 'Training on lines and sentences produced an RMSLE of ', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regLines(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones numLinesPerReview ] [ weights ] = [ numVotes ]
    """
    trainMatrix = np.asmatrix([(1, len(review['text'].splitlines())) for review in reviewsList])
    weightVector, RMSLE = doTrain(trainMatrix, scoreVector)
    print 'Training on just lines produced a RMSLE of', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regLinesSqrLines(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones numLines numLines^2 ] [ weights ] = [ numVotes ]
    """
    trainMatrix = np.asmatrix([(1,
                                len(review['text'].splitlines()),
                                len(review['text'].splitlines())**2)
                               for review in reviewsList])

    weightVector, RMSLE = doTrain(trainMatrix, scoreVector)
    print 'Training on lines & lines^2 produced a RMSLE of', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def regLinesLogLines(reviewsList, scoreVector):
    """
     Train a linear regression on [ Ones numLines log(numLines) ] [ weights ] = [ numVotes ]
    """
    trainMatrix = np.asmatrix([(1,
                                len(review['text'].splitlines()),
                                math.log(len(review['text'].splitlines())+1))
                               for review in reviewsList])

    weightVector, RMSLE = doTrain(trainMatrix, scoreVector)
    print 'Training on lines & logLines produced a RMSLE of', RMSLE
    return weightVector, RMSLE

# ---------------------------------------------------------------------------------------------------
def doTrain(X, Y):
    W = findWeightsLinReg(X, Y)
    return W, findRootMeanSquareLogError(X, W, Y)

# ---------------------------------------------------------------------------------------------------
def findRootMeanSquareLogError(X,W,Y):
    """
     To do element-by-element arithmetic, each given 'np.matrix' must be casted to 'np.array'
    """
    squaredLogError = (np.log(np.asarray(X * W)+1) - np.log(np.asarray(Y)+1)) ** 2
    rootMeanSquareLogError = math.sqrt(sum(squaredLogError) / len(Y))
    return rootMeanSquareLogError

# ---------------------------------------------------------------------------------------------------
def findWeightsLinReg(X,Y): return (X.T * X).I * X.T * Y

# ---------------------------------------------------------------------------------------------------
def importJSONFiles(filenames): return [[json.loads(line) for line in open(filename)] for filename in filenames]

# ---------------------------------------------------------------------------------------------------
def getYELPFiles():
    trainingBusinessPath = '../../Kaggle_Yelp_Comp/yelp_training_set/yelp_training_set_business.json'
    trainingCheckinPath  = '../../Kaggle_Yelp_Comp/yelp_training_set/yelp_training_set_checkin.json'
    trainingReviewPath   = '../../Kaggle_Yelp_Comp/yelp_training_set/yelp_training_set_review.json'
    trainingUserPath     = '../../Kaggle_Yelp_Comp/yelp_training_set/yelp_training_set_user.json'
    trainingFiles = [trainingBusinessPath, trainingCheckinPath, trainingReviewPath, trainingUserPath]

    testingBusinessPath  = '../../Kaggle_Yelp_Comp/yelp_test_set/yelp_test_set_business.json'
    testingCheckinPath   = '../../Kaggle_Yelp_Comp/yelp_test_set/yelp_test_set_checkin.json'
    testingReviewPath    = '../../Kaggle_Yelp_Comp/yelp_test_set/yelp_test_set_review.json'
    testingUserPath      = '../../Kaggle_Yelp_Comp/yelp_test_set/yelp_test_set_user.json'
    testFiles = [testingBusinessPath, testingCheckinPath, testingReviewPath, testingUserPath]
    return trainingFiles, testFiles

########  GLOBALS  #########
BUSINESS = 0
CHECKIN = 1
REVIEW = 2
USER = 3

if __name__ == "__main__":
    main()
