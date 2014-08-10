'''
8/10/14, 8:24 AM
Ethan Petuchowski
load_data

Full framework for testing different models for the Titanic Kaggle competition
'''
from sklearn import cross_validation
from sklearn.ensemble import GradientBoostingClassifier, RandomForestClassifier
from sklearn.linear_model import LogisticRegression
import pandas as pd, numpy as np, csv, os

# go to this file's location
# stackoverflow.com/questions/5137497
os.chdir(os.path.dirname(os.path.realpath(__file__)))

class Titanic(object):

    @staticmethod
    def preprocess(df):
        """ convert the categorical variables to dummy variables """
        assert isinstance(df, pd.DataFrame), 'can only preprocess a dataframe'
        # First of all turn Sex from a string-factor variable into a simple Indicator
        df.Sex = (df.Sex == 'female').astype(int)
        # Label missing Embarked as having embarked from most common place
        # if there are null values
        if len(df.Embarked[ df.Embarked.isnull() ]) > 0:
            # all rows with null values, should instead receive the mode value
            df.Embarked[ df.Embarked.isnull() ] = df.Embarked.dropna().mode().values
        """
        * Instead of translating categories to numbers, make dummy-variable columns
                and then remove the redundant one
        * Specifically, we will go
                from Categorical(['C', 'Q', 'S']), =-> to Indicator('C'), Indicator('Q')
        """
        df = pd.concat([df,pd.get_dummies(df.Embarked)], axis=1).drop(['S'], axis=1)
        # All the ages with no data -> make the median of all Ages
        median_age = df.Age.dropna().median()
        if len(df.Age[ df.Age.isnull() ]) > 0:
            df.loc[ df.Age.isnull(), 'Age' ] = median_age
        df = df.drop(['Name', 'Ticket', 'Cabin', 'Embarked', 'PassengerId'], axis=1)
        return df

    @staticmethod
    def fill_fares(df):
        """
        Fill missing Fares with median of their respective Pclass

        Note: This doesn't need to be done for the Training Set because
              none of the fares are missing in that set
        """
        assert isinstance(df, pd.DataFrame), 'df must be a DataFrame, was a %s' % type(df)
        if len(df.Fare[ df.Fare.isnull() ]) > 0:
            median_fare = np.zeros(3) # => array([ 0.,  0.,  0.])
            for f in range(0,3):
                median_fare[f] = df[ df.Pclass == f+1 ]['Fare'].dropna().median()
                df.loc[ (df.Fare.isnull()) & (df.Pclass == f+1 ), 'Fare'] = median_fare[f]
        return df

    def prop_correct(self, predictions, test_cv):
        '''
        everything mentioned is in reference to /this/ round of cross-validation

        :param predictions: test set predictions
        :param test_cv: indices of training set used as test set
        :return: proportion of predictions that matched the true target
        '''
        target_arr = self.train_target[test_cv]
        num_incorrect = sum([i != j for i, j in zip(target_arr, predictions)])
        prop_incorrect = float(num_incorrect) / len(predictions)
        return 1 - prop_incorrect

    def k_fold_cross_validate(self, k=8):
        ''' get results of using the provided model on each cv-segment '''
        cv = cross_validation.KFold(len(self.train_arr), n_folds=k, indices=False)
        for train_cv, test_cv in cv:
            fitted = self.model.fit(self.train_attr[train_cv], self.train_target[train_cv])
            predictions = fitted.predict(self.train_attr[test_cv])
            self.results.append(self.prop_correct(predictions, test_cv))

    def model_name(self):
        full_str = str(type(self.model))
        left = full_str.rfind('.')+1
        right = full_str.rfind("'")
        return full_str[left:right]

    def __init__(self,
                 train_loc='data/train.csv',
                 test_loc='data/test.csv',
                 out_loc='data/output',
                 out_name='logReg',
                 model=None):
        '''
        1. read data
        2. preprocess data
        3. print cross-validation scores for model on data
        4. save csv of predictions for test-set
        '''
        self.out_file = out_loc+'/'+out_name+'.csv'
        self.model = model if model is not None else LogisticRegression()
        print 'Model: %s' % self.model_name()
        self.raw_train_df = pd.read_csv(train_loc, header=0)
        self.raw_test_df = pd.read_csv(test_loc, header=0)

        # collect the test data's PassengerIds before dropping it
        self.ids = self.raw_test_df.PassengerId.values

        # preprocess the data
        self.prep_train_df = self.preprocess(self.raw_train_df)
        self.prep_test_df = self.preprocess(self.raw_test_df)
        self.prep_test_df = self.fill_fares(self.prep_test_df)

        # convert dataframes back to numpy arrays
        self.train_arr = self.prep_train_df.values
        self.test_arr = self.prep_test_df.values

        # separate record-attributes from record-targets
        self.train_attr = self.train_arr[:,1:]
        self.train_target = self.train_arr[:,0]

        # cross-validate
        self.results = []
        self.k_fold_cross_validate()
        print "Mean CV result: %.3f\n" % float( np.array(self.results).mean() )
        self.fitted_model = self.model.fit(self.train_attr, self.train_target)
        self.survival_predictions = self.fitted_model.predict(self.test_arr).astype(int)
        self.write_submission()

    def write_submission(self):
        with open(self.out_file, 'wb') as predictions_file:
            csv_writer = csv.writer(predictions_file)
            csv_writer.writerow(['PassengerId', 'Survived'])
            csv_writer.writerows(zip(self.ids, self.survival_predictions))

if __name__ == '__main__':
    Titanic(model=LogisticRegression(), out_name='log_reg')

    # TODO figure out what to put for the parameters like
    # min_samples_split, min_samples_leaf
    Titanic(model=GradientBoostingClassifier(max_features=3, min_samples_leaf=5, min_samples_split=5), out_name='grad_boost')
    Titanic(model=RandomForestClassifier(n_estimators=300, n_jobs=-1), out_name='rand_forest')

    # TODO use a blended trainer like in
    #   https://github.com/emanuele/kaggle_pbr/blob/master/blend.py
    #
