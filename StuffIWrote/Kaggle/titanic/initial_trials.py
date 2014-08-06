"""
8/4/14
Ethan Petuchowski

This script is based on the following IPython Notebook:
nbviewer.ipython.org/github/agconti/kaggle-titanic/blob/master/Titanic.ipynb
The comments and descriptions of what's going on are my own.

That Notebook was created as an intro to doing data science with Python
through the Titanic Kaggle competetition [1]. My plan is to use it here as a
starting point for my own investigation. It does a fair amount of data cleaning
and exploratory analysis that I don't know how to do, so I intend to learn that
here. Also, it tries out a few standard competitive machine learning
algorithms. I am curious what a "Good" way to do this is, so I am excited to
get to that point in the material.

I have decided to make this a regular .py file instead of an .ipynb so that it
doesn't require a special viewer or link to view it. It can just be read and
run raw using `python file.py`.

The goal of this Kaggle competition is to predict which passengers on the
Titanic survived. The reason it is not totally random is that there weren't
enough lifeboats for everyone. Apparently, women, children, and members of
the upper-class were more likely to survive.

[1]: kaggle.com/c/titanic-gettingStarted

"""

########################
##  Part 0:  IMPORTS  ##
########################

# plotting based on matlab
import matplotlib.pyplot as plt

# fast matrices and math operations for python
import numpy as np

# data processing utilities compatible with numpy
import pandas as pd

# another plotting library
import statsmodels.api as sm
from statsmodels.nonparametric.kde import KDEUnivariate
from statsmodels.nonparametric import smoothers_lowess

# just to get them into the namespace
from pandas import Series, DataFrame

# brings R's "formulas" syntax for describing statistical models to Python
from patsy import dmatrices

# implementations of a lot of machine learning algorithms
# as well as containers for holding and managing the datasets
from sklearn import datasets, svm

# make sure we're in the right place on the file system
import os
os.chdir('/Users/Ethan/Dropbox/CSyStuff/ProgrammingGit/StuffIWrote/Kaggle/titanic')

# import KaggleAux
import sys
ka_loc = '/Users/Ethan/code/personal_project_use/libraries_to_use/Python/KaggleAux'
if ka_loc not in sys.path:
    sys.path.append(ka_loc)
from kaggleaux import modeling as ka


#################################################
##  Part 1:  WRANGLE THE DATA (not much to do) ##
#################################################


df = pd.read_csv("data/train.csv")

# tickets are going to be annoying to figure out,
# & cabin has a lot of NaN values, so drop those columns
df = df.drop(['Ticket','Cabin'], axis=1)

# remove all rows containing people who have any NaN values
df = df.dropna()

########################################################
##  Part 2:  (not so good) EXPLORATORY DATA ANALYSIS  ##
########################################################

def plots_1():
    """
    A bunch of example charts; one of them is useful (the 3rd),
    and it seems to me (at this point) the rest aren't.
    """

    # allocate figure, figsize=(w,h) in inches
    fig = plt.figure(figsize=(18,6), dpi=1600)

    alpha_scatterplot = 0.2
    alpha_bar_chart   = 0.55

    # on a (2 X 3) plotting-grid, `ax1` occupies only
    #   the upper-left corner grid-cell
    ax1 = plt.subplot2grid((2,3),(0,0))

    """
    df.Survived -- "indicator" variable of each passenger's survival.
    df.Survived.value_counts() -- gives us

            0    424
            1    288

        which is simply the IPython way of saying the pandas.core.series.Series

            [424, 288]

        So we could have alternatively said

            a = Series([424, 288])
            a.plot(kind='bar')

        The default plot type is a line-graph
    """
    df.Survived.value_counts().plot(kind='bar', alpha=alpha_bar_chart)
    ax1.set_xlim(-1, 2) # reset margins in matplotlib to deal with bug in 1.3.1
    plt.title("Distribution of Survival, (1 = Survived)")

    """
    allocate space in the 2 X 3 grid at row 0, col 1 for another chart
    let it be a scatter-plot of survival (0 or 1) vs age
    I don't find this chart revealing of anything useful, but I'll leave it
    """
    plt.subplot2grid((2,3),(0,1))
    plt.scatter(df.Survived, df.Age, alpha=alpha_scatterplot)
    plt.ylabel("Age")
    plt.grid(b=True, which='major', axis='y') # format grid style
    plt.title("Survial by Age,  (1 = Survived)")

    # horizontal bar chart of how many people were in each Pclass
    ax3 = plt.subplot2grid((2,3),(0,2))
    df.Pclass.value_counts().plot(kind="barh", alpha=alpha_bar_chart)
    ax3.set_ylim(-1, len(df.Pclass.value_counts()))
    plt.title("Class Distribution")

    # this chart is two columns wide
    plt.subplot2grid((2,3),(1,0), colspan=2)

    """
    Here, we plot a "kernel desnsity estimate" (think density function with
    smoothing) of the Age distribution in each Pclass.

    To create this, for each Pclass, we filter our Series to only people in
    that Pclass, then we plot it. Every plot becomes a new Layer on the
    existing plot.
    """
    df.Age[df.Pclass == 1].plot(kind='kde')
    df.Age[df.Pclass == 2].plot(kind='kde')
    df.Age[df.Pclass == 3].plot(kind='kde')
    plt.xlabel("Age")
    plt.title("Age Distribution within classes")
    plt.legend(('1st Class', '2nd Class','3rd Class'),loc='best')

    # bar chart of number of passengers who got on at each station
    ax5 = plt.subplot2grid((2,3),(1,2))
    df.Embarked.value_counts().plot(kind='bar', alpha=alpha_bar_chart)
    plt.title("Passengers per boarding location")

def plots_2():
    """ the same plot as above, only horizontal this time """
    plt.figure(figsize=(6,4))
    fig, ax = plt.subplots()
    df.Survived.value_counts().plot(kind='barh', color="blue", alpha=.65)
    plt.title("Survival Breakdown (1 = Survived, 0 = Died)")

def plots_3():
    """
    Here are two incorrect charts; don't use them for anything
    because they are wrong, i.e. the statistics they present are not the true values.
    """

    # this figure should be 18" X 6", but it's clearly being scaled by Canopy
    # or something because it is not showing up nearly that big
    fig = plt.figure(figsize=(18,6))

    # create a subplot of a figure with 1 row and 2 columns
    # put this subplot in the first location
    ax1 = fig.add_subplot(121)

    """
    this graph is INCORRECT because the values for 0 on male are being added
    to the values for 1 on female, and presented as the 1 bar, and vice versa
    for the other one. Not sure how to fix it, so I'm moving on for now.
    I believe the problem is with the plot() function, because if I do

        .value_counts(sort=False)

    then the Series come out of both genders in the same order, but their
    associated plots are /still/ backwards from each other.

    It might be possible to use

        plt.set_xticklabels([0,1])

    or something (this is used in the next batch of plots), but I haven't tried
    it out.
    """
    df.Survived[df.Sex == 'male'].value_counts().plot(kind='barh',label='Male')
    df.Survived[df.Sex == 'female'].value_counts().plot(kind='barh', color='#FA2379',label='Female')
    plt.title("Who Survived? with respect to Gender, (raw value counts) "); plt.legend(loc='best')


    # adjust graph to display the proportions of survival by gender
    # (this graph is also broken, not sure if it is in the same way as the above one)
    ax2 = fig.add_subplot(122)
    ( df.Survived[df.Sex == 'male'].value_counts() / float(df.Sex[df.Sex == 'male'].size) ).plot(kind='barh',label='Male')
    ( df.Survived[df.Sex == 'female'].value_counts() / float(df.Sex[df.Sex == 'female'].size) ).plot(kind='barh', color='#FA2379',label='Female')
    plt.title("Who Survived proportionally? with respect to Gender"); plt.legend(loc='best')

def plots_4():
    """
    plot survival vs death counts diced by male/female and 1st-2nd-class/3rd-class

    These charts appear to be accurate (sigh), and reveal quite plainly that
    the order of survival ratio is

        1. Female 1st or 2nd class
        2. Female 3rd class
        3. Male 1st or 2nd class
        4. Male 3rd class
    """
    fig = plt.figure(figsize=(18,4), dpi=1600)
    alpha_level = 0.65

    ax1=fig.add_subplot(141)
    female_highclass = df.Survived[df.Sex == 'female'][df.Pclass != 3].value_counts()
    female_highclass.plot(kind='bar', label='female 1st or 2nd class', color='#FA2479', alpha=alpha_level)
    ax1.set_xticklabels(["Survived", "Died"], rotation=0)
    ax1.set_xlim(-1, len(female_highclass)) # this seems to shape the bars to be skinnier
    plt.title("Survival wrt Gender and Class"); plt.legend(loc='best')

    ax2=fig.add_subplot(142, sharey=ax1) # plot ax2 shares y-axis with plot ax1
    female_lowclass = df.Survived[df.Sex == 'female'][df.Pclass == 3].value_counts()
    female_lowclass.plot(kind='bar', label='female, 3rd class', color='pink', alpha=alpha_level)
    ax2.set_xticklabels(["Died","Survived"], rotation=0)
    ax2.set_xlim(-1, len(female_lowclass))
    plt.legend(loc='best')

    ax3=fig.add_subplot(143, sharey=ax1)
    male_lowclass = df.Survived[df.Sex == 'male'][df.Pclass == 3].value_counts()
    male_lowclass.plot(kind='bar', label='male, 3rd class',color='lightblue', alpha=alpha_level)
    ax3.set_xticklabels(["Died","Survived"], rotation=0)
    ax3.set_xlim(-1, len(male_lowclass))
    plt.legend(loc='best')

    ax4=fig.add_subplot(144, sharey=ax1)
    male_highclass = df.Survived[df.Sex == 'male'][df.Pclass != 3].value_counts()
    male_highclass.plot(kind='bar', label='male 1st or 2nd class', alpha=alpha_level, color='steelblue')
    ax4.set_xticklabels(["Died","Survived"], rotation=0)
    ax4.set_xlim(-1, len(male_highclass))
    plt.legend(loc='best')


#################################
##  Part 3:  MACHINE LEARNING  ##
#################################

### Algorithm 1: Logistic/Logit Regression

# store regression results for analysis later
results = {}

#def logit_regression():

"""
Model `formula`, this is where we're using the "patsy" library.
This formula system is documented in detail here:

    http://patsy.readthedocs.org/en/latest/formulas.html#formulas

Here the ~ sign is an = sign, and the features of our dataset
are written as a formula to predict survived.

The C() lets our regression know that those variables are categorical.

For our first, simplest try, we're just saying the Survival indicator
is some simple linear combination of the other variables
"""
formula = 'Survived ~ C(Pclass) + C(Sex) + Age + SibSp  + C(Embarked)'


"""
Here we create two patsy "design matrices" in dataframes

According to the statsmodels docs [1]

    To fit most of the models covered by statsmodels, you will need to
    create two design matrices. The first is a matrix of endogenous
    variable(s) (i.e. dependent, response, regressand, etc.). The second
    is a matrix of exogenous variable(s) (i.e. independent, predictor,
    regressor, etc.).

    The patsy module provides a convenient function to prepare design
    matrices using R-like formulas.

[1]: statsmodels.sourceforge.net/devel/gettingstarted.html#design-matrices-endog-exog

So the variable `y` we're creating is (I'm pretty sure) the same as df.Survived.as_dataframe()

And the variable `x` is a dataframe with columns

    Intercept        # always 1
    C(Pclass)[T.2]   # indicator of whether Pclass == 2
    C(Pclass)[T.3]   # also indicators...
    C(Sex)[T.male]
    C(Embarked)[T.Q]
    C(Embarked)[T.S]
    Age              # integer (e.g. 38)
    SibSp            # number of siblings/spouses aboard (max = 5, mean = 0.5)

"""
y, x = dmatrices(formula, data=df, return_type='dataframe')

# create a statsmodels' Logit regression model on all the data
model = sm.Logit(y,x)

# run the algorithm
res = model.fit()

# save the result for outputing predictions later
results['Logit'] = [res, formula]

# print a nice little summary like you'd find with STATA
print res.summary()


###############
# Logit Plots #
###############

# Plot Predictions Vs Actual
plt.figure(figsize=(18,4));
plt.subplot(121, axisbg="#DBDBDB")

# generate predictions from `res`, our fitted model
# we're using the training data again to form these predictions, I believe
# but it's just quick-and-dirty-work to see whether the logit was even
# halfway-decent.
ypred = res.predict(x)

"""
this is saying
    make predicted values (in range (0,1)) blue circles
    make true values (in set {0,1}) red circles
    both with low opacity
"""
plt.plot(x.index, ypred, 'bo', x.index, y, 'mo', alpha=.25);

# this makes it look like R
plt.grid(color='white', linestyle='dashed')
plt.title('Logit predictions, Blue: \nFitted/predicted values: Red');

# Residuals
ax2 = plt.subplot(122, axisbg="#DBDBDB")

"""
this is the "deviance residual", i.e.

    "the measure of deviance contributed from each observation"

where

    "deviance is a quality of fit statistic for a model"

It shall be a red dashed line on a white dashed grid

I believe we should use this plot to see whether there is any trend
    in the residuals.
"""
plt.plot(res.resid_dev, 'r-')
plt.grid(color='white', linestyle='dashed')
ax2.set_xlim(-1, len(res.resid_dev))
plt.title('Logit Residuals');


# Hey I've got an idea, let's just make more plots...

fig = plt.figure(figsize=(18,9), dpi=1600)
a = .2

fig.add_subplot(221, axisbg="#DBDBDB")

"""
this is the "kernel density estimator", just like was used above,
to create a nice smoothed density plot of the predictions
the y-values look incorrect, but I'm guessing the shape is right
"""
kde_res = KDEUnivariate(res.predict())
kde_res.fit()

# I think the "support" is simply the domain in which the
# density is greater than 0.
plt.plot(kde_res.support,kde_res.density)
plt.fill_between(kde_res.support,kde_res.density, alpha=a)
plt.title("Distribution of our Predictions")

# show that predicted survival probabilities are much lower
# for males than females
fig.add_subplot(222, axisbg="#DBDBDB")
plt.scatter(res.predict(),x['C(Sex)[T.male]'] , alpha=a)
plt.grid(b=True, which='major', axis='x')
plt.xlabel("Predicted chance of survival")
plt.ylabel("Gender Bool")
plt.title("The Change of Survival Probability by Gender (1 = Male)")

# show that surval probabilities are strongly dependent on Pclass
fig.add_subplot(223, axisbg="#DBDBDB")
plt.scatter(res.predict(),x['C(Pclass)[T.3]'] , alpha=a)
plt.xlabel("Predicted chance of survival")
plt.ylabel("Class Bool")
plt.grid(b=True, which='major', axis='x')
plt.title("The Change of Survival Probability by Lower Class (1 = 3rd Class)")

# show that predicted survival probabilities are
# inversely correlated with Age
fig.add_subplot(224, axisbg="#DBDBDB")
plt.scatter(res.predict(),x.Age , alpha=a)
plt.grid(True, linewidth=0.15)
plt.title("The Change of Survival Probability by Age")
plt.xlabel("Predicted chance of survival")
plt.ylabel("Age")


################################
##  Part 4:  RUN ON TEST SET  ##
################################

test_data = pd.read_csv("data/test.csv")

# "Add our independent variable to our test data. (It's usually left
# blank by Kaggle because it's the value you're trying to predict.)"
# 1.23 is just a random value, it could have been anything
test_data['Survived'] = 1.23

# use model to make prediction on test set
compared_results = ka.predict(test_data, results, 'Logit')

# convert model to Series for easy output
compared_results = Series(compared_results)

# output and submit to Kaggle
compared_results.to_csv("data/output/logitregres.csv")
