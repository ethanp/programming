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

    # if there are null values (the training set has 2)
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

    df = df.drop(['Name', 'Ticket', 'Embarked', 'PassengerId'], axis=1)
    return df

# Load the Data

train_df = pd.read_csv('data/train.csv', header=0)
rt = train_df.copy()
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

    My conclusion from this thing, is that there are three categories,
    young, middle, old, and so I will create appropriate indicator bins for them
    """

    train_df.Age.hist()
    plt.show()

    dd = pd.DataFrame(train_df.Survived)
    dd['Age'] = train_df.Age
    gb = dd.groupby(['Age'])
    gb.mean().plot()


def bin_ages(df):
    """
    Perhaps this is something the Random Forest is already
    taking care of on its own?
    """
    df['Young'] = (df.Age < 15).astype(int)
    df['Old'] = (df.Age > 50).astype(int)
    df = df.drop['Age']
    return df


def family_analysis():
    """
    Including the family size in the form of Parch or SibSp
    don't seem to be too helpful

    I'm going to leave them out in the future
    """

    # look at just Parch
    train_df.Parch.hist()
    dd = pd.DataFrame(train_df.Survived)
    dd['Parch'] = train_df.Parch
    gb = dd.groupby(['Parch'])
    gb.mean().plot()

    # look at (Parch + SibSp)
    dd = pd.DataFrame(train_df.Survived)
    dd['Fam'] = train_df.Parch + train_df.SibSp
    dd.Fam.hist()
    gb = dd.groupby(['Fam'])
    gb.mean().plot()

def cabin_analysis():
    """
    My initial hunch is that having a cabin means you're wealthier
    and more likely to have made it onto a safety raft.

    The analysis here confirms that (more than I'd expected)
    """
    dd = pd.DataFrame(rt.Survived)
    dd['Cabin'] = rt.Cabin.isnull().astype(int)
    dd.Cabin.hist()
    gb = dd.groupby(['Cabin'])
    gb.mean().plot()

def embarked_analysis():
    """
    What we see here, is that a person from C is significantly more likely to survive.
    Also, most people are from S, which is where you're least likely to survive.
    """
    dd = pd.DataFrame(rt.Survived)
    dd['Embarked'] = rt.Embarked
    dd.groupby(['Embarked']).mean()
    dd.groupby(['Embarked']).mean().plot()
    dd.groupby(['Embarked']).count()
