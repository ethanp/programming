"""
Generate plots to see what can be improved

For the Titanic Kaggle competition

Ethan Petuchowski
August 6, 2014
"""

import pandas as pd
import numpy as np


# go to this file's location
# stackoverflow.com/questions/5137497
import os
os.chdir(os.path.dirname(os.path.realpath(__file__)))

def preprocess(df):

    # we must convert the categorical variables to dummy variables

    # First of all turn Sex from a string-factor variable into a simple Indicator
    df.Sex = (df.Sex == 'female').astype(int)

    # All missing Embarked -> just make them embark from most common place

    # if there are null values
    if len(df.Embarked[ df.Embarked.isnull() ]) > 0:

        # all rows with null values, should instead receive the mode value
        df.Embarked[ df.Embarked.isnull() ] = df.Embarked.dropna().mode().values

    """
    instead of trainslating categories to numbers, I'm making dummy cols
            and then removing the redundant one
    Specifically, we will go
            from Categorical(['C', 'Q', 'S']),
            to Indicator('C'), Indicator('Q')
    """
    df = pd.concat([df,pd.get_dummies(df.Embarked)], axis=1).drop(['S'], axis=1)


    # All the ages with no data -> make the median of all Ages
    median_age = df.Age.dropna().median()
    if len(df.Age[ df.Age.isnull() ]) > 0:
        df.loc[ df.Age.isnull(), 'Age' ] = median_age

    df = df.drop(['Name', 'Ticket', 'Cabin', 'Embarked', 'PassengerId'], axis=1)
    return df

# Load the Data

train_df = pd.read_csv('data/train.csv', header=0)
train_df = preprocess(train_df)

test_df = pd.read_csv('data/test.csv', header=0)

# collect the test data's PassengerIds before dropping it
ids = test_df.PassengerId.values
test_df = preprocess(test_df)

# for all the missing Fares -> assume median of their respective Pclass
if len(test_df.Fare[ test_df.Fare.isnull() ]) > 0:
    median_fare = np.zeros(3) # => array([ 0.,  0.,  0.])
    for f in range(0,3):
        median_fare[f] = test_df[ test_df.Pclass == f+1 ]['Fare'].dropna().median()
    for f in range(0,3):
        test_df.loc[ (test_df.Fare.isnull()) & (test_df.Pclass == f+1 ), 'Fare'] = median_fare[f]


################
##  PLOTTING  ##
################
import pylab as plt


def age_analysis():
    """
    AGE

    My conclusion from this thing, is that age, as a variable on-its-own
    has no impact on chance of survival.
    """

    train_df.Age.hist()
    plt.show()

    # select just the Survived and Age columns
    survival_age = zip(train_df.Survived.values, train_df.Age.values)

    # round ages to the tens
    binned = [(surv, int(age)/10*10) for surv, age in survival_age]

    gb = pd.DataFrame(binned, columns=['Survived', 'Age']).groupby(['Age'])
    print 'Total counts in each age bin:'
    print gb.count()
    print
    print 'Fraction Surviving:'
    print gb.mean()

    gb.mean().plot()
    plt.show()
