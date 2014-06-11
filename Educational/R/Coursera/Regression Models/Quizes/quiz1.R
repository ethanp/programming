# 6/3/14
# Ethan Petuchowski
# Regression Models Quiz 1

##############
# Question 1 #
##############

x <- c(0.18, -1.54, 0.42, 0.95)
w <- c(2, 1, 3, 1)

# Give the value of μ that minimizes the least squares equation ∑ [ w_i * (x_i − μ)^2 ]

mean(x)     # => 0.0025 (INCORRECT)
sum(w * x)  # => 1.03 (not an option)
sum(x)      # => .01 (not an option)
mean(w*x)   # => 0.2575 (not an option)

for (i in c(
    .147,   # 3.717  => minimum
    .0025,  # 3.863
    .3,     # 3.880
    1.077)  # 9.769
    ) { print(sum(w*((x-i)^2))) }


##############
# Question 2 #
##############

x <- c(0.8, 0.47, 0.51, 0.73, 0.36, 0.58, 0.57, 0.85, 0.44, 0.42)
y <- c(1.39, 0.72, 1.55, 0.48, 1.19, -1.59, 1.23, -0.65, 1.49, 0.05)

# Fit the regression THROUGH THE ORIGIN and GET THE SLOPE,
# treating y as the outcome and x as the regressor.
# Do not center the data.

fit <- lm(y ~ x)
fit$coefficients[[2]] # => -1.713 (INCORRECT?!)
# Why it's wrong ==> (I didn't notice the "through the origin" part [now highlighted])

fit2 <- lm(y ~ x-1) # this is a special command to omit the intercept (sneakay. it was in the lecture)
fit2$coefficients[[1]] # => .8263

##############
# Question 3 #
##############

data(mtcars)
fit <- lm(mtcars$mpg ~ mtcars$wt)
fit$coefficients[[2]] # => -5.344


##############
# Question 4 #
##############

# Consider data with an outcome (Y) and a predictor (X). 
# The standard deviation of the predictor is one half that of the outcome.
# The correlation between the two variables is 0.5.

# What value would the slope coefficient for the regression model with Y
# as the outcome and X as the predictor?

# I.e. FIND THE SLOPE AS A FUNCTION OF RELATIVE STANDARD DEVIATIONS AND CORRELATION

# from http://en.wikipedia.org/wiki/Simple_linear_regression we have

# \hat{\beta} = r_{xy} \frac{s_y}{s_x}
#             = 0.5 * 2
#             = [[1]]


##############
# Question 5 #
##############

# Students were given two hard tests and scores were normalized to have
# empirical mean 0 and variance 1. The correlation between the scores
# on the two tests was 0.4. What would be the expected score on Quiz 2
# for a student who had a normalized score of 1.5 on Quiz 1?

# I.e. predict y_2 given y_1 and Corr(1,2), mean_1,2 and variance_1,2

# => My guess right now is .6, because that is (1.5 * 0.4), but I am totally making that up.

# from http://en.wikipedia.org/wiki/Simple_linear_regression we have

# \hat{\beta} = r_{xy} \frac{s_y}{s_x}
#             = 0.4 * 1
#             = 0.4

# \hat{\alpha} = \bar{y} - \hat{\beta} \bar{x}
#              = 0 - 0.4 * 0

# \hat{y} = \hat{\alpha} + \hat{\beta} x
#         = 0 + 1.5 * 0.4
#         = [[0.6]]    ==> so I'm sticking with my initial guess


##############
# Question 6 #
##############

# Consider the data given by the following
x <- c(8.58, 10.46, 9.01, 9.64, 8.86)
# What is the value of the first measurement if x were normalized (to have mean 0 and variance 1)?
scale(x)[1] # => -.9719


##############
# Question 7 #
##############

x <- c(0.8, 0.47, 0.51, 0.73, 0.36, 0.58, 0.57, 0.85, 0.44, 0.42)
y <- c(1.39, 0.72, 1.55, 0.48, 1.19, -1.59, 1.23, -0.65, 1.49, 0.05)

# find the intercept of y on x

fit <- lm(y ~ x)
fit$coefficients[[1]] # => 1.567



##############
# Question 8 #
##############

# find the intercept of a linear regression when x & y have mean 0

# \hat{\alpha} = \bar{y} - \hat{\beta} \bar{x}
#              = 0 - z * 0
#              = [[0]]


##############
# Question 9 #
##############

x <- c(0.8, 0.47, 0.51, 0.73, 0.36, 0.58, 0.57, 0.85, 0.44, 0.42)

# What value minimizes the sum of the squared distances between these points and itself?

# That's a great question, to which I'm not sure of the answer.
# Is it the mean? I'd guess no but I'm not sure.
# It is the mean, recall that the mean is the "center of mass".

mean(x) # => 0.573


###############
# Question 10 #
###############

# he said it in lecture.

