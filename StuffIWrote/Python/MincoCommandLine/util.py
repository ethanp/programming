import datetime
import os
from mcl_cli import *

# set home dir
HOME_PATH = '/Users/Ethan/Dropbox/School Help Files/Tracker'
TASKS_PATH = HOME_PATH+'/Tasks'
CSVs_PATH = HOME_PATH+'/Days'
CSV_FORMAT = ['group', 'task', 'location', 'start time', 'end time', 'block time']


# TODO unfinished (not sure what this will do yet, 'just figure it'll be useful)
def get_csv(date=''):
    # get today's date, format it like the filename
    if date is '':
        today = datetime.date.today()
    else:
        # TODO parse-date
        pass

    todays_date = str(today.month) + '-' + str(today.day) + '-' + str(today.year)
    csvs = os.listdir(CSVs_PATH)
    for this_csv in csvs:
        csv_date = this_csv[:this_csv.find('.')]  # assumes the name is properly formatted
        if csv_date is todays_date:
            # read info into DataFrame
            pass


def group_path(group):
    '''
    returns full-path version of the given group name
    '''
    return TASKS_PATH+'/'+group
