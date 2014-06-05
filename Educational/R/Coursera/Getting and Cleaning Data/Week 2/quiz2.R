# 6/4/14
# Ethan Petuchowski
# Coursera: Getting and Cleaning Data
# Quiz 2 (SQL, Web APIs, Web Scraping)

################
#  Question 1  #
################

# didn't work for me
# I think it's Nov 7 13 though


################
#  Question 2  #
################

library(sqldf)
dataLoc <- '/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Getting and Cleaning Data/Week 2/data/getdata-data-ss06pid.csv'
acs <- read.csv(dataLoc)
sqldf("select * from acs where AGEP < 50")


################
#  Question 3  #
################

# only too obvious


################
#  Question 4  #
################

con = url("http://biostat.jhsph.edu/~jleek/contact.html")
htmlCode = readLines(con)
close(con)
nchar(htmlCode[10])
nchar(htmlCode[20])
nchar(htmlCode[30])
nchar(htmlCode[100])


################
#  Question 5  #
################

thisDir <- '/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Getting and Cleaning Data/Week 2/'
setwd(thisDir)
if (!file.exists("data")) { dir.create("data") }
fileUrl <- "https://d396qusza40orc.cloudfront.net/getdata%2Fwksst8110.for"
dataDest <- './data/data.csv'
download.file(fileUrl, destfile = dataDest, method = "curl") # curl for https

# found this at stackoverflow.com/questions/14383710
data <- read.fwf(file=dataDest, skip=4, widths=c(12, 7,4, 9,4, 9,4, 9,4))
sum(data[4]) # the output is not exact and I don't know why