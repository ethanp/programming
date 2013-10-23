# coding=utf-8
## "A time-tracker built with the user in mind" (note: I am the user)

## Started: 10/16/13

import shutil
import subprocess
import sys
import os
import datetime
import glob
import csv
import argparse
import pandas as pd

from util import *


# example usage
#applescripts.createEvent(calName='theCalName',
#             eventTitle='theEventTitle',
#             eventNotes='theEventNotes',
#             eventLocation='theEventLocation',
#             startDate='November 4, 2013 6:30:00 PM',
#             endDate='November 5, 2013 1:00:00 AM')


# tested
def ls(group=''):
    '''
    RETURNS: map of number to its task-name

    list tasks, with call-numbers for each
    in a tree-like format so the groups are displayed

    should allow for group typed and listed to be of any arrangement of capitalizations
    '''
    task_counter = 0
    task_dict = {}
    # print task-tree and create dictionary
    paths = os.listdir(TASKS_PATH)
    groups = [g for g in paths if os.path.isdir(TASKS_PATH+'/'+g)]
    groups_lower = [g.lower() for g in groups]
    if group:
        if group.lower() in groups_lower:
            path = TASKS_PATH+'/'+groups[groups_lower.index(group.lower())]
            print 'searching', path
        else:
            print 'group:', group, 'was not found'
            return False
    else:
        path = TASKS_PATH

    dir_tree = subprocess.check_output(['tree', path]).splitlines()
    for line in dir_tree:
        line = line.replace('\\','')
        UP_TO_DOT = line.find('.')
        NO_TREE = line.rfind(' ') + 1
        if UP_TO_DOT != -1:                 # found a task
            task_dict[task_counter] = line[NO_TREE:]  # add it to the dictionary
            print line[:UP_TO_DOT], task_counter          # print without the ".task"
            task_counter += 1
        else:
            print line
    return task_dict


# TODO use pandas to calculate (and print!) from the CSV(s)
# TODO pandas: read in table: pg. 33
# names1880 = pd.read_csv('names/yob1880.txt', names=['name', 'sex', 'births'])
# TODO unfinished
def ll(group=''):
    '''
    ls, but instead of showing the tree and the numbers,
    prints table of time totals, start dates, due dates, etc
    '''
    # create a data structure to store the info
    # open each task
    # TODO haven't checked if this is written correctly
    data = None
    for group in os.listdir(TASKS_PATH):
        group_path = TASKS_PATH+group
        for task in os.listdir(group_path):
        # get its start date and due date (if there is one)
            # TODO I need to make
            dayFrame = pd.read_table(group_path+task, sep='\n', header=None)
            if data is None:
                data = dayFrame.copy()  # TODO made up: don't know the method's name
            data = pd.merge(data, dayFrame)  # pg. 28, TODO sloppy: there must be better way to do it

    # calculate totals
    per_task = data.groupby('task name').sum()  # don't think this is right
    # print it all out
    pass


# TODO pandas: SUM(minutes) GROUP_BY(title): page 33
# .groupby('sex').births.sum()
# TODO unfinished
def printDay(date='*'):
    '''
    print tabulated vrsn of given day’s CSV w/ line#s (default to today)
    '''
    # open the right CSV
    files = glob.glob('Days/'+date)
    print files
    with open(files[-1], 'rb') as the_csv:
        reader = csv.reader(the_csv)
        lines = the_csv.readlines()
        print lines
        for row in reader:
            print row

    # do some counting over the CSV file

    # e.g. SUM(time) GROUPBY(subject,title)
    for line in lines:
        print line


# tested
def add_group(name):
    '''
    if it doesn't exist, create a new folder in the Tasks directory
    otw do nothing
    capitalization will NOT be checked
    '''
    new_group_path = group_path(name)
    if not os.path.exists(new_group_path):
        os.makedirs(new_group_path)


# TODO figure out how due_dates are going to be formatted on their way in
#   so they can be printed properly to the file
# TODO unfinished
def add_task(group, name, due_date='', note=''):
    '''
    if task doesn't exist, add a new task in the group specified
    capitalization will be left as-specified in the command
    if group doesn't exist, ask whether to create one
    '''

    group_path = TASKS_PATH + '/' + group
    file_path = group_path + '/' + name + '.task'
    tmp_file = 'tmp.scpt'

    # TODO turn the date into a usable format for both the .task and the reminder
    now_object = datetime.datetime.now()
    today = str(now_object.month)+'-'+str(now_object.day)+'-'+str(now_object.year)

    # TODO check against other capitalizations

    # ask to create group if it doesn't exist
    if not os.path.exists(group_path):
        response = raw_input('group "'+group+'" does not exist, create it? (y/n)')
        if response is 'y':
            add_group(group)
        else:
            print 'Ok, cancelling.'

    if os.path.exists(file_path):
        print 'Task already exists.'
        return

    with open(file_path, 'w+') as task_file:
        writer = csv.writer(task_file, delimiter='\n')
        writer.writerow([today, due_date])

    # TODO enable (after testing) the creation of a reminder
    ## generate script to create reminder
    #script = applescripts.createReminder(
    #                todoList=group,
    #                eventTitle=name,
    #                note=note,
    #                dueDate=dueDate)

    ## create reminder
    #with open(tmp_file, 'wr') as script_file:
    #    script_file.write(script)
    #subprocess.call(['osascript','tmp.scpt'])
    #os.remove(tmp_file)


# tested
def delete_group(name):
    print group_path(name)
    if not os.path.exists(group_path(name)):
        print 'group "'+name+'" does not exist.'
        return False
    else:
        response = raw_input('are you sure you want to delete group "'+name+'"? (y/n) ')
        if response == 'y':
            print 'deleting "'+name+'".'
            shutil.rmtree(group_path(name))
        return True


# TODO unfinished
def editTask(group, name, newGroup=None, newName=None, dueDate=None, note=None):
    # edit task file
    ## edit reminder
    #script = applescripts.editReminder(
    #    # args go here
    #)
    pass


# TODO unfinished
def begin(number=None, group=None, name=None):
    '''
    "start the clock" for the given task
    this is really just printing a piece of the CSV line out, I think
        i.e. the name and start time,
             but not the end time or block time etc.

    the name can optionally include the group name inside it, i.e. name="NN HW#3"
        this should still work (with any capitalization)
    '''
    ## open today's CSV
    csv = get_csv()
    # print info
    pass


# TODO unfinished
def end():
    '''
    print the end time and block time for the last line of the CSV
    also print the info out for the user to see
    '''
    # open today's CSV
    csv = get_csv()
    # write info
    # append to .task file
    # create calendar event
    # edit reminder to have updated total time
    pass


# TODO unfinished
def move(minutes):
    '''
    change the start time of the currently running task by {minutes}
    '''
    csv = get_csv()
    # edit the last line of the CSV
    pass


# TODO unfinished
def show():
    '''
    print time for current task (block, day, total)
    '''
    csv = get_csv()
    # parse last line of today's CSV
    pass


# TODO unfinished
def cancel():
    '''
    cancel current task
    '''
    csv = get_csv()
    # remove last line of CSV
    pass


# TODO unfinished
def did():
    '''
    pos-hoc add time a task was done
        e.g. $ mcl did NN HW#3 10:30 15:15
    '''
    # create a line (in the right place) in today's CSV
    # create a calendar event
    # add to the .task file
    pass


# TODO unfinished
def create_command_line_options():
    options = argparse.ArgumentParser(description='Manage Reminders, Calendar, and CSV in one go')
    options.add_argument('ls', help='list tasks, with call-numbers for each')
    options.add_argument('lsGroup', help='for given group, list tasks, with call-numbers for each')
    options.add_argument('ll', help='prints table of time totals, start dates, due dates, etc')
    options.add_argument('llGroup', help='for given group, print table of totals, dates, etc')
    options.add_argument('print', help='print tabulated vrsn of today’s CSV w/ line#s')
    options.add_argument('addTask', help='create new task')
    options.add_argument('addGroup', help='create new task-group')
    options.add_argument('begin', help='begin counting for given task number or task name')
    options.add_argument('end', help='stop current running task')
    options.add_argument('move', help='edit start time of current running task')
    options.add_argument('show', help='print time for current running task (block, day, total)')
    options.add_argument('cancel', help='cancel current running task')
    options.add_argument('remove', help='remove block from CSV and calendar')
    options.add_argument('finish', help='remove task')
    options.add_argument('did', help='post-hoc add time a task was done')


# TODO unfinished
def get_task(name='task1'):
    pass


def main(argv):
    create_command_line_options()
    ll()
    #addTask('Homework Assns', 'NN Assn4', note='Nothing Here')


if __name__ == "__main__":
    main(sys.argv[1:])
