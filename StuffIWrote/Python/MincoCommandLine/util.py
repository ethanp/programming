import datetime
import os
import re
from mcl_cli import *

# set home dir
HOME_PATH = '/Users/Ethan/Dropbox/School Help Files/Tracker'
TASKS_PATH = HOME_PATH+'/Tasks'
CSVs_PATH = HOME_PATH+'/Days'
OLD_TASKS_PATH = HOME_PATH+'/Old_Tasks'
CSV_FORMAT = ['group', 'task', 'location', 'start time', 'end time', 'block time']
TODAY = ''

# TODO make this REGEX work (snag task-name from task-file)
a = re.compile(r'regex goes here')
a.match('a string goes here, I believe')
#TASK_NAME_REGEX = '?<TASK_NAME>(.*)\.task'.compile()


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

def get_group_path(group):
    '''
    if group exists (capitalization agnostic), returns its full-path
    else returns False
    '''
    paths = os.listdir(TASKS_PATH)
    groups = [g for g in paths if os.path.isdir(TASKS_PATH + '/' + g)]
    groups_lower = [g.lower() for g in groups]
    if group:
        if group.lower() in groups_lower:
            return TASKS_PATH + '/' + groups[groups_lower.index(group.lower())]
        else:
            print 'group:', group, 'was not found'
            return False


# tested on sample_task
def get_task_path(name, group=''):
    '''
    capitalization agnostic
    find the task with the given name
    searches within group if specified
    group needn't be specified if the name is unique anyway
    if found return task's path
    else return false
    '''
    if not group:
        group_paths = os.listdir(TASKS_PATH)
        groups = [g for g in group_paths if os.path.isdir(TASKS_PATH+'/'+g)]
    else:
        groups = list(group)
    group_paths = [get_group_path(g) for g in groups]
    found_list = []
    for group_path in group_paths:
        tasks = os.listdir(group_path)

        # TODO replace with the TASK_NAME_REGEX above
        task_names_lowered = [t[:t.rfind('.')].lower() for t in tasks]

        if name.lower() in task_names_lowered:
            task_path = group_path + '/' + tasks[task_names_lowered.index(name.lower())]
            found_list.append(task_path)
    if len(found_list) > 1:
        print 'This task was found in multiple groups: `util.get_task_path()`'
        return False
    if len(found_list) == 0:
        print "This task wasn't found: `util.get_task_path()`"
        return False
    return found_list[0]


def create_group_path(group=''):
    return TASKS_PATH+'/'+group if group else TASKS_PATH


def create_task_path(name, group):
    return create_group_path(group)+'/'+name+'.task'

def todays_date():
    # turn the date into a usable format for both the .task and the reminder
    n = datetime.datetime.now()
    today = str(n.month) + '-' + str(n.day) + '-' + str(n.year)
    return today
