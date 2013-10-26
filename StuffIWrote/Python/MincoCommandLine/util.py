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

def get_group_path(group=''):
    '''
    returns full-path version of the given group name
    '''
    # print task-tree and create dictionary
    if not group:
        return TASKS_PATH
    paths = os.listdir(TASKS_PATH)
    groups = [g for g in paths if os.path.isdir(TASKS_PATH + '/' + g)]
    groups_lower = [g.lower() for g in groups]
    if group:
        if group.lower() in groups_lower:
            return TASKS_PATH + '/' + groups[groups_lower.index(group.lower())]
        else:
            print 'group:', group, 'was not found'
            return False

def create_group_path(group=''):
    return TASKS_PATH+'/'+group if group else TASKS_PATH
