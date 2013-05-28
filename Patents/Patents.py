# Trawling Specified US Patent Class/Subclass for Recently Allowed Applications
# May 2013
#
import urllib2
import sys
from bs4 import BeautifulSoup
import HTMLParser

    classNo = raw_input("Input Class Number:")
    subclassNo = raw_input ("Input Subsclass Number:")
    print "Searching in US Class:", classNo, "/", subclassNo
    classTerm = "TERM1="+ classNo + "%2F" + subclassNo + "&FIELD1=CCLS&co1=AND&d=PG01"

    url= "http://appft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&" + classTerm

    #url1 = 'http://appft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&p=1&u=%2Fnetahtml%2FPTO%2Fsearch-bool.html&r=0&f=S&l=50&TERM1=514%2F290&FIELD1=CCLS&co1=AND&TERM2=&FIELD2=&d=PG01'

    usock = urllib2.urlopen(url)

data = usock.read()
usock.close()

# print data
soup = BeautifulSoup(data)

strSoup = str(soup)

linkQueue = []
for line in strSoup.splitlines():
    if len(line) > 3:
        if line.__contains__('href'):
            splitLine = line.split('"')
            for spl in splitLine:
                if spl.__contains__('netacgi/nph') and spl.__contains__('AND&amp'):
                    spl = spl.replace('&amp', '&')
                    spl = spl.replace(';', '')
                    linkQueue += [spl]

linkQueue = list(set(linkQueue))
goQueue = []

for link in linkQueue:
    goHere = 'http://appft.uspto.gov' + link
    goQueue += [goHere]

for go in goQueue: print go

serList = []
for go in goQueue:
    usock = urllib2.urlopen(go)
    data = usock.read()
    usock.close()
    soup = BeautifulSoup(data)
    splines = str(soup).splitlines()    # turns the html into a list of lines
    for line in splines:
        if line.__contains__('Serial No.'):
            serialIndex = splines.index(line)
        if line.__contains__('Series Code'):
            seriesIndex = splines.index(line)
            break # just to speed it up a bit

    serialLine = splines[serialIndex+2]
    serialNo = serialLine[6:12]     # finds characters 6 through 11 of this line
    seriesLine = splines[seriesIndex+2]
    seriesNo = seriesLine[6:8]
    if serialNo.isdigit() and seriesNo.isdigit():  # needed when you click the "next" page
        print seriesNo+serialNo
        serList += [seriesNo+serialNo]

print serList

## TODO: save this list to a file so it doesn't need to be generated every time

import os


## Iterate through the list of relevant patent #s from some other file
for applicationNumber in serList:

    ## Download data
    gsutil = "/usr/local/bin/gsutil cp -R gs://uspto-pair/applications/"+applicationNumber+"* ."
    print gsutil
    os.system(gsutil)
    print

    ## unzip transaction history
    zipfile = applicationNumber+".zip"
    transactionHistory = applicationNumber+"/"+applicationNumber+"-transaction_history.tsv"
    unzip = "unzip -n "+zipfile+" "+transactionHistory
    print unzip
    if os.path.exists(zipfile):
        os.system(unzip)
        print

        ## if #lines containing "Allowance" > 0 && Allowance < 4 months old
        allowancesFound = 0
        for line in open(transactionHistory,'r'):

            if line[0] is 'D': continue

            date = line.split('\t')[0]
            month = int(date[:2])
            year = int(date[6:10])

            if year == 2013:
                if line.find("Allowance") > -1:
                    print "found a recent Allowance"
                    print line
                    print "Application Number:", applicationNumber
                    allowancesFound += 1
                    break

            else: break

            # TODO else if it's been "Abandoned" ignore it from now  append it to yet another file for the ones to check next time

        if not allowancesFound: print "none found"

        # TODO append the current # to another file