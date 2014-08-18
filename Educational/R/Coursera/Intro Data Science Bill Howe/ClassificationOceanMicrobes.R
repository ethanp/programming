# Ethan Petuchowski
# Started: 8/17/14
#     Due: 8/28/14
# "Classification of Ocean Microbes"
# Quiz as part of Coursera's
# Introduction to Data Science
# Taught by Bill Howe of the University of Washington
#
# This assignment involves data from the SeaFlow environmental flow cytometry instrument

# load recommended libraries
library(caret)
library(rpart)
library(tree)
library(randomForest)
library(e1071)
library(ggplot2)


# Step 1
data <- read.csv('/Users/Ethan/code/Coursera/datasci_course_materials/assignment5/seaflow_21min.csv')
summary(data)

# Step 2
# based on http://topepo.github.io/caret/splitting.html
trainIndices <- createDataPartition(data$file_id, p=.8, list=FALSE, times=1)
dataTrain <- data[ trainIndices,]
dataTest  <- data[-trainIndices,]
mean(dataTrain$time)

# Step 3
ggplot(dataTrain, aes(pe, chl_small, color=pop)) + geom_point()

# Step 4
# TODO
