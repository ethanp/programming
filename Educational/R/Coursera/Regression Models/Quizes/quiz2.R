# 6/5/14
# Ethan Petuchowski
# Regression Models Quiz 2

# Some preliminary notes:

# 1. two sided hypothesis test
# as a worst case this happens in 01_07_b with raw code
# I think you can use the summary(lm(data)) to do this

# 2. residual standard deviation:
# residual: diff btn observed and predicted outcome (i.e. Y-\hat{Y})

# 3. 95% confidence interval

# 4. coefficient interpretation:
# b-naught is the predicted value of y for x = 0
# b-1 is the expected change in y for a 1 unit change in x
# multiplying X by A divdes b-1 by A

# 5. ratio of the sum of the squared errors

# 6. sum of the residuals
# if an intercept is included, the sum of the residuals is zero

##############
# Question 1 #
##############

x <- c(0.61, 0.93, 0.83, 0.35, 0.54, 0.16, 0.91, 0.62, 0.62)
y <- c(0.67, 0.84, 0.6, 0.18, 0.85, 0.47, 1.1, 0.65, 0.36)

# Give a P-value for the two sided hypothesis test of whether 
# β1 from a linear regression model is 0 or not.
fit <- lm(y ~ x)
summary(fit)$coefficients

##############
# Question 2 #
##############

# Give the estimate of the residual standard deviation.'

# "the square root of the estimated variance of the random error"
summary(fit)$sigma  # => .223  (this is listed)
?summary.lm   # this one divides the sse by n-p


sd(residuals(fit))      # => .029  (this is not listed)
?residuals    # this one divides the sse by n-1

##############
# Question 3 #
##############

# In the mtcars data set, fit a linear regression model of
# weight (predictor) on mpg (outcome).

data(mtcars)
fit <- lm(mpg ~ wt, data = mtcars) 

# Get a 95% confidence interval for the expected mpg at the average weight.
# What is the lower endpoint?

newdata <- data.frame(wt=mean(mtcars$wt))
predict(fit, newdata, interval = ("confidence"))[2]


##############
# Question 4 #
##############

data(mtcars)
fit <- lm(mpg ~ wt, data = mtcars)
summary(fit)
?mtcars


##############
# Question 5 #
##############

# Consider again the mtcars data set and a linear regression model
# with mpg as predicted by weight (1,000 lbs).
# A new car is coming weighing 3000 pounds.
# Construct a 95% prediction interval for its mpg.
# What is the upper endpoint?

data(mtcars)
fit <- lm(mpg ~ wt, data = mtcars) 
newdata <- data.frame(wt=3)
predict(fit, newdata, interval = ('prediction'))[3]

##############
# Question 6 #
##############

# Consider again the mtcars data set and a linear regression model
# with mpg as predicted by weight (in 1,000 lbs).
# A “short” ton is defined as 2,000 lbs.
# Construct a 95% confidence interval for the expected change in mpg
# per 1 short ton increase in weight.
# Give the lower endpoint.

data(mtcars)
wtOvr2 <- mtcars$wt / 2
fit <- lm(mtcars$mpg ~ wtOvr2)
coef <- summary(fit)$coefficients

# coef estimate minus t-dist @ .975 confidence * stderror of coef estimate
coef[2,1] - qt(.975, df = fit$df) * coef[2,2]


##############
# Question 9 #
##############

# Refer back to the mtcars data set with mpg as
# an outcome and weight (wt) as the predictor.
data(mtcars)

# About what is the ratio of the the sum of the squared errors
# when comparing a model with just an intercept (denominator)
y <- mtcars$mpg; x <- mtcars$wt; n <- length(y)
fit <- lm(y ~ x - 1)
beta1 <- summary(fit)$coefficients[1]
e <- y - beta1*x
sse1 <- sum(e^2)
# to the model with the intercept and slope (numerator)?

y <- mtcars$mpg; x <- mtcars$wt; n <- length(y)
beta1 <- cor(y, x) * sd(y) / sd(x)
beta0 <- mean(y) - beta1 * mean(x)
e <- y - beta0 - beta1 * x
sse2 <- sum(e^2)

# this is not what the question asked for (it asked for sse2/sse1)
# but hey, it worked
sqrt(sse2)/sqrt(sse1)
