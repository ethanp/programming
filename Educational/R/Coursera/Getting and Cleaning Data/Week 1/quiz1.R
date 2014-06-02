# Ethan Petuchowski
# 6/2/14

#########
#  CSV  #
#########

# Download the 2006 microdata survey about housing for the state of Idaho
housingUrl <- "https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06hid.csv"
setwd("/Users/ethan/code/non_apple/programming/Educational/R/Coursera/Getting and Cleaning Data/Week 1/")
list.files()
if (!file.exists("data")) { dir.create("data") }
list.files()
download.file(housingUrl, destfile="./data/housing.csv", method = "curl") # curl for https
dateDownloaded <- date() # 6/2/14 11:09:40
dataHousing <- read.csv("./data/housing.csv")
hist(dataHousing$VAL)
daClean <- dataHousing$VAL
daClean <- daClean[complete.cases(daClean)]
d <- dataHousing[complete.cases(dataHousing$VAL),]$VAL
length(d[d == 24]) # => 53

###########
#  Excel  #
###########

# Download the Excel spreadsheet on Natural Gas Aquisition Program
gasUrl <- "https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2FDATA.gov_NGAP.xlsx"
download.file(gasUrl, destfile="./data/gas.xlsx", method = "curl") # curl for https
gasFile <- "./data/gas.xlsx"
library(xlsx)
rows <- 18:23
cols <- 7:15
dat <- read.xlsx(gasFile, sheetIndex=1, rowIndex=rows, colIndex=cols)
sum(dat$Zip*dat$Ext,na.rm=T)


#########
#  XML  #
#########

# Read the XML data on Baltimore restaurants
restaUrl <- "https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Frestaurants.xml"
restaFile <- "./data/restaurants.xml"
download.file(restaUrl, destfile=restaFile, method="curl")
library(XML)
xmlRest <- xmlTreeParse(restaFile, useInternalNodes=TRUE)
root <- xmlRoot(xmlRest)
zipcodes <- xpathSApply(root, "//zipcode", xmlValue)
twoOnes <- zipcodes[zipcodes == "21231"]
length(twoOnes)  # => 127


###########
#  FREAD  #
###########

# 2006 microdata survey about housing for the state of Idaho
microUrl <- "https://d396qusza40orc.cloudfront.net/getdata%2Fdata%2Fss06pid.csv"
microFile <- "./data/micro.csv"
download.file(microUrl, destfile=microFile, method="curl")
library(data.table); library(microbenchmark)
DT <- fread(microFile)
DT$SEX <- as.numeric(DT$SEX)
system.time(replicate(100, mean(DT$pwgtp15,by=DT$SEX)))
system.time(replicate(100, sapply(split(DT$pwgtp15,DT$SEX),mean)))
system.time(replicate(100, tapply(DT$pwgtp15,DT$SEX,mean)))
system.time(replicate(100, DT[,mean(pwgtp15),by=SEX])) # this is fastest because it's a real data.table method

