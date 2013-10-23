from collections import Counter
import math
from Project import importJSONFiles, BUSINESS, REVIEW


def probScoreGivenCategories(filenames):
    """
     P( score | storeCategories ) = P(score) * PI_{categories} [ P( each-category | score ) ]
    """

    # import the necessary files
    reviews, businesses = importJSONFiles([filenames[REVIEW],filenames[BUSINESS]])
    print '2 files imported'

    # make features out of the most popular 30 categories, and put the rest in the 'other' pile
    #------------------------------------------------------------------------------
    BIGBIN = 'bigBin'
    businessIDToCat = {}
    for business in businesses:
        businessIDToCat[business['business_id']] = business['categories']
    categoryLists = businessIDToCat.values()
    categories = []
    for categoryList in categoryLists:
        categories += categoryList
    categorySet = set(categories)
    categoryCounts = Counter()
    for category in categorySet:
        categoryCounts[category] = categories.count(category)
    pop30WithValues = categoryCounts.most_common(30)
    pop30 = [x for x,y in pop30WithValues]
    myCats = pop30 + ['other']
    #------------------------------------------------------------------------------
    print 'found the 30 most popular categories'


    # map reviews to categories
    #------------------------------------------------------------------------------
    # (reviewScore, business_id)
    scoreBusinessTuples = [(review['votes']['useful'],review['business_id']) for review in reviews]

    # Want: (reviewScore, category)
    scoreCatTuples = []
    for score, busID in scoreBusinessTuples:
        categories = businessIDToCat[busID]
        for category in categories:
            if category in pop30:
                scoreCatTuples += [(score, category)]
            else:
                scoreCatTuples += [(score, 'other')]
        #------------------------------------------------------------------------------
    print "matched each review's score to its categories"



    # form the priors from score counts
    #------------------------------------------------------------------------------
    initialPriorCounter = Counter()
    for score, busID in scoreBusinessTuples:
        initialPriorCounter[score] += 1
    numReviews = len(scoreBusinessTuples)
    initialPriorScores = Counter()
    for score in initialPriorCounter.keys():
        initialPriorScores[score] = float(initialPriorCounter[score]) / numReviews
    # (important parts of the distribution up for display in the doc)
    for score in sorted(initialPriorScores.keys()):
        print '\t%3d: %.4f, %d' % (score, initialPriorScores[score], initialPriorCounter[score])
    priorCounter = Counter()
    for score in sorted(initialPriorScores.keys()):
        if score < 4:
            priorCounter[score] = initialPriorCounter[score]
        else:
            priorCounter[BIGBIN] += initialPriorCounter[score]
    priorScores = Counter()
    myScores = sorted(priorCounter.keys())
    for score in myScores:
        priorScores[score] = float(priorCounter[score]) / numReviews
        print '\t%s: %.4f, %d' % (score, priorScores[score], priorCounter[score])
        #------------------------------------------------------------------------------
    print 'found the values of all the priors:\n'



    # find all the P( category | score )
    #------------------------------------------------------------------------------
    condProbs = {}
    for score in myScores:
        condProbs[score] = Counter()

    for score, busID in scoreBusinessTuples:
        if score > 3: score = BIGBIN
        for category in businessIDToCat[busID]:
            if not category in myCats: category = 'other'
            if not condProbs[score][category]:
                condProbs[score][category] = 1  # Laplace smoothing for numerator
            condProbs[score][category] += 1

    for score in myScores:
        for category in myCats:                         # Laplace smoothing for denominator
            condProbs[score][category] /= float(priorCounter[score] + len(myCats))
            if condProbs[score][category] < .000000001: print 'this could be a problem: %s, %s' % (score, category)
    print 'found all the conditional probabilities'
    #------------------------------------------------------------------------------




    # actually classify
    #------------------------------------------------------------------------------
    # this must be done by review ID so I can compare them to the true scores
    # However, there IS no 'review_id' so I'm going to (hopefully uniquely...)
    #   identify each review by its (user_id, business_id) tuple as my 'key' in the hash-map
    # I'm going to do this in LOG_SPACE because I think the computer prefers float-addition to float-multiplication
    reviewScoreProbVectors = {}
    reviewScoreClassifications = {}
    for review in reviews:
        busCats = businessIDToCat[review['business_id']]
        review_id = (review['user_id'], review['business_id'])
        reviewScoreProbVectors[review_id] = Counter()  # this is the prob-vector
        for score in myScores:
            if score > 3: score = BIGBIN
            reviewScoreProbVectors[review_id][score] = math.log(priorScores[score])
            for category in busCats:
                if category not in myCats: category = 'other'
                reviewScoreProbVectors[review_id][score] += math.log(condProbs[score][category])
        reviewScoreClassifications[review_id] = reviewScoreProbVectors[review_id].most_common(1)[0][0]
        #------------------------------------------------------------------------------



    # print the results
    #------------------------------------------------------------------------------
    correctCount = len([review for review in reviews
                        if review['votes']['useful']
                           == reviewScoreClassifications[(review['user_id'], review['business_id'])]])
    nonZeroCorrectCount = len([review for review in reviews
                               if review['votes']['useful'] == reviewScoreClassifications[(review['user_id'], review['business_id'])]
        and review['votes']['useful'] != 0])
    nonZeroTotalCount = len([review for review in reviews if review['votes']['useful'] != 0])

    # "Got 95393 correct out of 229907 "
    # "Got 215 correct that weren't zeros, out of 134537 non-zeros"
    print 'Got %d correct out of %d' % (correctCount, len(reviews))
    print "Got %d correct that weren't zeros, out of %d non-zeros" % (nonZeroCorrectCount,nonZeroTotalCount)
    #------------------------------------------------------------------------------

