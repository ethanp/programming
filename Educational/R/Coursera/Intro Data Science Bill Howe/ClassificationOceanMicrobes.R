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
model_rpart <- rpart(fol, method="class", data=dataTrain)
print(model_rpart)


########################
# Step 5 (evaluate Decision Tree on test data)
preds_rpart <- predict(model_rpart, newdata=dataTest, type="class")
ab <- preds_rpart == dataTest$pop
accuracy_rpart <- sum(ab) / length(ab)


########################
# Step 6 (Random Forest)

# build random forest
model_rf <- randomForest(fol, data=dataTrain)
preds_rf <- predict(model_rf, newdata=dataTest, type="class")
ab <- preds_rf == dataTest$pop
accuracy_rf <- sum(ab) / length(ab)
# print importance of each variable in the trained model
importance(model_rf)


########################
# Step 7 (Support Vector Machine)

model_svm <- svm(fol, data=dataTrain)
preds_svm <- predict(model_svm, newdata=dataTest, type="class")
ab <- preds_svm == dataTest$pop
accuracy_svm <- sum(ab) / length(ab)


########################
# Step 8.a. (confusion matrices)

confusion_rpart <- table(rpart=preds_rpart, true = dataTest$pop)
confusion_rf <- table(rf=preds_rf, true = dataTest$pop)
confusion_svm <- table(svm=preds_svm, true = dataTest$pop)

########################
# Step 8.b. (remove file_id 208, and retrain SVM)

ggplot(data, aes(x = time, y = chl_big, colour = pop)) + geom_point()

data_clean <- data[data$file_id != 208,]
trainIndices_clean <- createDataPartition(data_clean$file_id, p=.8, list=FALSE, times=1)
dataTrain_clean <- data_clean[ trainIndices_clean,]
dataTest_clean  <- data_clean[-trainIndices_clean,]

model_svm_clean <- svm(fol, data=dataTrain_clean)
preds_svm_clean <- predict(model_svm_clean, newdata=dataTest_clean, type="class")
ab <- preds_svm_clean == dataTest_clean$pop
accuracy_svm_clean <- sum(ab) / length(ab)

improvement <- accuracy_svm_clean - accuracy_svm
improvement
