# coding=utf-8
## "A time-tracker built with the user in mind" (where I am the user)

## Started: 10/16/13

'''
=============
Sample Usages
=============

    # list tasks, with call-numbers for each
    $ mcl ls

    # list group’s tasks, with call-numbers for each
    $ mcl ls --group NN

    # ls, but with time totals, start dates, due dates, etc
    $ mcl ll

    # add task if it doesn’t exist
    $ mcl addTask NN --task HW#3 --duedate 10/23

    # add group if it doesn’t exist
    $ mcl addGroup Alg

    # begin counting for first task
    $ mcl begin 1

    # adds task if it doesn’t exist
    $ mcl begin NN HW#3

    # stops counting
    $ mcl end

    # move start time forward 15 mins
    $ mcl move 15

    # move start time backward 15 mins
    $ mcl move -15

    # print tabulated vrsn of today’s CSV w/ line#s
    $ mcl print

    # delete line 4 of CSV printed above
    $ mcl remove 4

    # remove task 1
    $ mcl finish 1

    # print time for current task (block, day, total)
    $ mcl show

    # post-hoc add time a task was done
    $ mcl did NN HW#3 10:30 15:15

    # cancel current task
    $ mcl cancel


=============
DIR STRUCTURE
=============
Days/
    10-17-2013.csv
    10-18-2013.csv
    10-19-2013.csv

Tasks/
    NN/
        HW#3.task
        Ch 16.task

    Alg/


=========
CSV FILES
=========
group, task, location, start time, end time, block time
group, task, location, start time, end time, block time
...


==========
TASK FILES
==========
start date
due date (can be blank)
time for block 1
...
time for block n


'''

import subprocess # this is what you're supposed to use now instead of "import sys"
# http://docs.python.org/2/library/subprocess.html#replacing-older-functions-with-the-subprocess-module
import sys  # though I don't see a way to get the command line args from subprocess
import applescripts
import os
import datetime
import glob
import csv
import argparse


# set home dir
HOME_PATH = '/Users/Ethan/Dropbox/School Help Files/Tracker'
os.chdir(HOME_PATH)
os.listdir('.')

# example usage
#applescripts.createEvent(calName='theCalName',
#             eventTitle='theEventTitle',
#             eventNotes='theEventNotes',
#             eventLocation='theEventLocation',
#             startDate='November 4, 2013 6:30:00 PM',
#             endDate='November 5, 2013 1:00:00 AM')


def ls(directory=''):
    '''
    RETURNS: map of number to its task-number

    list tasks, with call-numbers for each
    in a tree-like format so the groups are displayed
    '''
    task_counter=0
    task_dict={}
    # print task-tree and create dictionary
    os.chdir('Tasks')
    dir_tree = subprocess.check_output(['tree']).splitlines()
    for line in dir_tree:
        line = line.replace('\\','')
        UP_TO_DOT = line.find('.')
        if UP_TO_DOT != -1:                 # found a task
            line = line[:UP_TO_DOT]         # subtract the ".task"
            task_dict[task_counter] = line  # add it to the dictionary
        print line
    os.chdir('..')
    return task_dict


def ll(group=''):
    '''
    ls, but instead of showing the tree and the numbers,
    prints table of time totals, start dates, due dates, etc
    '''
    # create a data structure to store the info
    # open each task
    # get its start date and due date (if there is one)
    # calculate its total time
    # store all that
    # print it all out
    pass


def printDay(date='*'):
    '''
    print tabulated vrsn of today’s CSV w/ line#s
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


def addGroup(name):
    '''
    if it doesn't exist, add a new folder in the current directory
    otw do nothing
    '''
    if not os.path.exists(name):
        os.makedirs(name)


def addTask(group, name, dueDate='November 4, 2013 6:30:00 PM', note=''):
    '''
    if it doesn't exist, add a new task in the group specified
    '''
    path = '.'
    file_path = path + '/' + name + '.task'
    tmp_file = 'tmp.scpt'

    if os.path.exists(file_path):
        return

    with open(file_path, 'wr') as task_file:
        task_file.write('created at'+str(datetime.datetime.now()))

    # generate script to create reminder
    script = applescripts.createReminder(
                    todoList=group,
                    eventTitle=name,
                    note=note,
                    dueDate=dueDate)

    # create reminder
    with open(tmp_file, 'wr') as script_file:
        script_file.write(script)
    subprocess.call(['osascript','tmp.scpt'])
    os.remove(tmp_file)


def editTask(group, name, newGroup=None, newName=None, dueDate=None, note=None):
    # edit task file
    # edit reminder
    script = applescripts.editReminder(
        # args go here
    )
    pass


def begin(number=None, group=None, name=None):
    '''
    "start the clock" for the given task
    this is really just printing a piece of the CSV line out, I think
        i.e. the name and start time,
             but not the end time or block time etc.
    '''
    # open today's CSV
    # print info
    pass


def end():
    '''
    print the end time and block time for the last line of the CSV
    also print the info out for the user to see
    '''
    # open today's CSV
    # write info
    # append to .task file
    # create calendar event
    # edit reminder to have updated total time
    pass


def move(minutes):
    '''
    change the start time of the currently running task by {minutes}
    '''
    # edit the last line of the CSV
    pass


def show():
    '''
    print time for current task (block, day, total)
    '''
    # parse last line of today's CSV
    pass


def cancel():
    '''
    cancel current task
    '''
    # remove last line of CSV
    pass


def did():
    '''
    pos-hoc add time a task was done
        e.g. $ mcl did NN HW#3 10:30 15:15
    '''
    # create a line (in the right place) in today's CSV
    # create a calendar event
    # add to the .task file
    pass


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


def main(argv):
    create_command_line_options()
    ll()
    #addTask('Homework Assns', 'NN Assn4', note='Nothing Here')


if __name__ == "__main__":
    main(sys.argv[1:])
