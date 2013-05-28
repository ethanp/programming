# Trawling Specified US Patent Class/Subclass for Recently Allowed Applications
# May 2013
#

import urllib2
import os
from bs4 import BeautifulSoup

def getNumberToSearchGoogleFor(splines):
    serialIndex,seriesIndex = 0,0
    for line in splines:                        # find locations of number and code
        if line.__contains__('Serial No.'):
            serialIndex = splines.index(line)
        if line.__contains__('Series Code'):
            seriesIndex = splines.index(line)
            break  # done with this page

    serialLine = splines[serialIndex+2]
    startIndex = serialLine.index('>') + 1
    stopIndex = serialLine.index('/') - 1
    serialNo = serialLine[startIndex:stopIndex]                 # pull serial# from the webpage
    seriesLine = splines[seriesIndex+2]
    startIndex = seriesLine.index('>') + 1
    seriesNo = seriesLine[startIndex:]

    if serialNo.isdigit() and seriesNo.isdigit():  # needed when you click the "next" page
        # print seriesNo+serialNo, 'was found on this page'
        return [seriesNo+serialNo]


def getPatentsAndNextPage(splines):
    linkQueue = []
    for line in splines:
        if len(line) > 3:
            if line.__contains__('href'):
                for spl in line.split('"'):
                    if spl.__contains__('netacgi/nph'):
                        if spl.__contains__('AND&amp'):
                            spl = spl.replace('&amp', '&')
                            spl = spl.replace(';', '')
                            linkQueue += [spl]
    linkQueue = list(set(linkQueue))      # remove duplicates
    return [baseURL + link for link in linkQueue]


def getHtmlAsListOfLines(go):
    uSock = urllib2.urlopen(go)
    data = uSock.read()
    uSock.close()
    soup = BeautifulSoup(data)
    return str(soup).splitlines()

def getStartingPointFromUserInput():
    classNo    = raw_input("Input Class Number:")
    subclassNo = raw_input("Input Subsclass Number:")

    print "Searching in US Class:", classNo, "/", subclassNo

    classTerm = "TERM1="+classNo+"%2F"+subclassNo+"&FIELD1=CCLS&co1=AND&d=PG01"
    searchURL = baseURL+"/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&"+classTerm
    #url1 = 'http://appft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=514%2F290&FIELD1=CCLS&co1=AND&TERM2=&FIELD2=&d=PG01'
    return [searchURL]


def searchPTOforSerialNums(searchDepthLimit):
    print 'Finding at most', searchDepthLimit, 'listings'
    goQueue = getStartingPointFromUserInput()
    searchableSerialNums = []
    while goQueue and (len(searchableSerialNums) < searchDepthLimit):
        go = goQueue.pop()
        # print go
        splines = getHtmlAsListOfLines(go)
        if go.__contains__('Page=Prev'):
            continue
        elif go.__contains__('TERM1') or go.__contains__('Page=Next'):
            goQueue += getPatentsAndNextPage(splines)
            print 'found another page of listings'
        else:
            searchableSerialNums += getNumberToSearchGoogleFor(splines)
            if len(searchableSerialNums) % 25 == 0:
                print len(searchableSerialNums), 'serial numbers found so far'

    print searchableSerialNums
    return searchableSerialNums

def searchFileForAllowances(transactionHistory, applicationNumber):
    for line in open(transactionHistory,'r'):
        if line[0] is 'D': continue     # skip the first line, column headers
        date = line.split('\t')[0]
        # month = int(date[:2])           # doesn't use month, but it could
        year = int(date[6:10])
        if year == 2013:
            print line
            if line.find("Allowance") > -1:
                print "found a recent Allowance"
                print "Application Number:", applicationNumber
                return True
        else:
            print "none found"
            return False


def findAllowances(searchableSerialNums):
    gsutilPrefix = "/usr/local/bin/gsutil cp -R gs://uspto-pair/applications/"  # Dad's
    # gsutilPrefix = '/Users/Ethan/gsutil/gsutil cp -R gs://uspto-pair/applications/'  # Ethan's

    gsutilPostfix = "* ."
    for applicationNumber in searchableSerialNums:

        ## Download data
        print 'Requesting zip-file from Google'
        gsutil = gsutilPrefix + applicationNumber + gsutilPostfix
        print gsutil
        os.system(gsutil)

        ## unzip transaction history
        zipfile = applicationNumber+".zip"
        transactionHistory = applicationNumber+"/"+applicationNumber+"-transaction_history.tsv"
        unzip = "unzip -n "+zipfile+" "+transactionHistory
        if os.path.exists(zipfile):
            os.system(unzip)
            searchFileForAllowances(transactionHistory,applicationNumber)
            os.system('rm -f '+zipfile)

baseURL = 'http://appft.uspto.gov'
if __name__ == '__main__':
    ## TODO: save this list to a file so it doesn't need to be generated every time
    # except if you are going to plug in different classes every time, maybe not

    searchableSerialNums = searchPTOforSerialNums(searchDepthLimit=150)
    searchableSerialNums = list(set(searchableSerialNums))
    print 'searching google for', len(searchableSerialNums), 'serial numbers'
    findAllowances(searchableSerialNums)
