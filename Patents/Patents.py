# Trawling Specified US Patent Class/Subclass for Recently Allowed Applications
# May 2013
# TODO: Untested at this point

import urllib2
import os
from bs4 import BeautifulSoup

def getNumberToSearchGoogleFor(splines):
    serialIndex,seriesIndex = 0,0
    for line in splines:
        if 'Serial No.' in line:
            serialIndex = splines.index(line)
        if 'Series Code' in line:
            seriesIndex = splines.index(line)
            break  # done reading this page
    serialLine = splines[serialIndex+2]
    startIndex = serialLine.index('>') + 1
    stopIndex  = serialLine.index('/') - 1
    serialNo   = serialLine[startIndex:stopIndex]
    seriesLine = splines[seriesIndex+2]
    startIndex = seriesLine.index('>') + 1
    seriesNo   = seriesLine[startIndex:]
    return [seriesNo+serialNo]

def getPatentsAndNextPage(splines, depthLimit):
    """ It would be possible to make this one line, but it would be ugly. """
    linkQueue = []
    serialPages = 0
    for line in splines:
        if len(line) > 3:
            if 'href' in line:
                if serialPages < depthLimit:
                    for spl in line.split('"'):
                        if 'netacgi/nph' in spl and 'AND&amp' in spl:
                            spl = spl.replace('&amp', '&')
                            spl = spl.replace(';', '')
                            linkQueue += [spl]
                            if not 'Page=Next' in spl:
                                serialPages += 1
                else: break
    linkQueue = list(set(linkQueue))      # remove duplicates
    return [baseURL + link for link in linkQueue]

def getHtmlAsListOfLines(go):
    uSock = urllib2.urlopen(go)
    data = uSock.read()
    uSock.close()
    soup = BeautifulSoup(data)
    return str(soup).splitlines()

def getStartingPointFromUserInput():
    classNo     = raw_input("Input Class Number:")
    subclassNo  = raw_input("Input Subclass Number:")
    searchDepth = raw_input("Input number of patents to search:")
    classTerm = "TERM1="+classNo+"%2F"+subclassNo+"&FIELD1=CCLS&co1=AND&d=PG01"
    searchURL = baseURL+"/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&"+classTerm
    return searchDepth, [searchURL]

def searchPTOforSerialNums():
    searchDepthLimit, goQueue = getStartingPointFromUserInput()
    searchableSerialNums = []
    searchDepthLimit = int(searchDepthLimit)
    while goQueue and (len(searchableSerialNums) < searchDepthLimit):
        go = goQueue.pop()  # pop(0) is O(n), pop() is O(1)
        splines = getHtmlAsListOfLines(go)
        if 'Page=Prev' in go:
            continue
        elif 'TERM1' in go or 'Page=Next' in go:
            goQueue += getPatentsAndNextPage(splines, searchDepthLimit)
        else:
            searchableSerialNums += getNumberToSearchGoogleFor(splines)
    return searchableSerialNums

def searchFileForAllowances(transactionHistory, applicationNumber):
    for line in open(transactionHistory,'r'):
        if line[0] is 'D': continue     # skip the first line, column headers
        date = line.split('\t')[0]
        year = int(date[6:10])
        if year == 2013:
            if line.find("Allowance") > -1:
                return True
        else:
            return False

def findAllowances(searchableSerialNums):
    gsutilPrefix = "/usr/local/bin/gsutil cp -R gs://uspto-pair/applications/"  # Dad's
    # gsutilPrefix = '/Users/Ethan/gsutil/gsutil cp -R gs://uspto-pair/applications/'  # Ethan's

    gsutilPostfix = "* ."
    for applicationNumber in searchableSerialNums:
        gsutil = gsutilPrefix + applicationNumber + gsutilPostfix
        os.system(gsutil)
        zipfile = applicationNumber+".zip"
        transactionHistory = applicationNumber+"/"+applicationNumber+"-transaction_history.tsv"
        unzip = "unzip -n "+zipfile+" "+transactionHistory
        if os.path.exists(zipfile):
            os.system(unzip)
            searchFileForAllowances(transactionHistory,applicationNumber)
            os.system('rm -f '+zipfile)

baseURL = 'http://appft.uspto.gov'
if __name__ == '__main__':
    searchableSerialNums = searchPTOforSerialNums()
    searchableSerialNums = list(set(searchableSerialNums))  # Doesn't do anything, but just in case
    findAllowances(searchableSerialNums)
