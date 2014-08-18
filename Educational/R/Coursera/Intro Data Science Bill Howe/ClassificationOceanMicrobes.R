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


########################
# Step 1 (load and initial data inspection)
data <- read.csv('/Users/Ethan/code/Coursera/datasci_course_materials/assignment5/seaflow_21min.csv')
summary(data)


########################
# Step 2 (separate train and test data)
# based on http://topepo.github.io/caret/splitting.html
# Note: p = proportion of data in the training set
trainIndices <- createDataPartition(data$file_id, p=.8, list=FALSE, times=1)
dataTrain <- data[ trainIndices,]
dataTest  <- data[-trainIndices,]
mean(dataTrain$time)


########################
# Step 3 (grouped scatterplot)
ggplot(dataTrain, aes(pe, chl_small, color=pop)) + geom_point()


########################
# Step 4 (train a Decision Tree)

# first we must create a "formula" object
fol <- formula(pop ~ fsc_small + fsc_perp + fsc_big + pe + chl_big + chl_small)

# "Recursive Partitioning and Regression Trees", this method "fits" the model
model <- rpart(fol, method="class", data=dataTrain)

print(model)


########################
# Step 5 (evaluate Decision Tree on test data)
preds <- predict(model, newdata=dataTest, type="class")
ab <- preds == dataTest$pop
accuracy <- sum(ab) / length(ab)


########################
# Step 6 (Random Forest)
# TDOOD
