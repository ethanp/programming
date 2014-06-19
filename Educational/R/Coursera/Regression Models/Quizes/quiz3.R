# 6/10/14
# Ethan Petuchowski
# Regression Models Quiz 3

##############
# Question 1 #
##############

# Consider the mtcars data set.
data(mtcars)

# Fit a model with mpg as the outcome that includes
# number of cylinders as a factor variable and weight as confounder.

fit <- lm(mpg ~ cyl + wt, mtcars) # TODO what does it *mean* to include `cyl` as a *factor* variable??
summary(fit)

# Give the adjusted estimate for the expected change in mpg comparing 8 cylinders to 4.
# TODO what is an "adjusted estimate"?

##############
# Question 2 #
##############

# Consider the mtcars data set.
data(mtcars)

# Fit a model with mpg as the outcome
# that includes number of cylinders as a factor variable
# and weight as confounder.


# Compare the adjusted by weight effect of 8 cylinders
# as compared to 4 the unadjusted.


# What can be said about the effect?.


##############
# Question 3 #
##############

# Consider the mtcars data set.
data(mtcars)

# Fit a model with mpg as the outcome
# that considers number of cylinders as a factor variable
# and weight as confounder.

# Consider the model
# with an interaction between cylinders and weight

# and one without.

# Give the P-value for the likelihood ratio test comparing the two models

# and suggest a model using 0.05 as a type I error rate significance benchmark.


##############
# Question 4 #
##############

# Consider the mtcars data set.
data(mtcars)

# Fit a model with mpg as the outcome
# that includes number of cylinders as a factor variable
# and weight inlcuded in the model as
lm(mpg ~ I(wt * 0.5) + factor(cyl), data = mtcars)

# How is the wt coefficient interpretted?


##############
# Question 5 #
##############

# Consider the following data set
x <- c(0.586, 0.166, -0.042, -0.614, 11.72)
y <- c(0.549, -0.026, -0.127, -0.751, 1.344)

# Give the "hat diagonal" for the most influential point


##############
# Question 6 #
##############

# Consider the following data set
x <- c(0.586, 0.166, -0.042, -0.614, 11.72)
y <- c(0.549, -0.026, -0.127, -0.751, 1.344)

# Give the slope "dfbeta" for the point with the highest hat value.


##############
# Question 7 #
##############

# Consider a regression relationship between Y and X
# with adjustment for a third variable Z

# and without adjustment for a third variable Z.


# Which of the following is true about comparing the regression
# coefficient between Y and X with and without adjustment for Z?
