"""
Modified from AstroDave's Random Forest, based in-part on agconti's Logistic Regression,
Here, I use Logistic Regression as provided by sklearn.

Because agconti's whole `statsmodels` library usage is just confusing me:
    How do you run it on the test data? Why don't they show it in any of the
    eg's in the documentation?

`sklearn` is far more straightforward.

Ethan Petuchowski
8/6/14
"""
import pandas as pd
import numpy as np
import csv as csv
from sklearn.linear_model import LogisticRegression

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

print 'Preprocessing...'

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

# Convert dataframes back to numpy arrays
train_arr = train_df.values
test_arr = test_df.values

print 'Training...'

# scikit-learn.org/stable/modules/generated/sklearn.linear_model.LogisticRegression.html
logit = LogisticRegression(penalty='l2')

X = train_arr[:,1:]
y = train_arr[:,0]
logit = logit.fit(X,y)

print 'Predicting...'
survival_predictions = logit.predict(test_arr).astype(int)


with open("data/output/mylogit3.csv", "wb") as predictions_file:
    csv_writer = csv.writer(predictions_file)
    submission_header = ["PassengerId","Survived"]
    csv_writer.writerow(submission_header)
    csv_writer.writerows(zip(ids, survival_predictions))

print 'Done.'
