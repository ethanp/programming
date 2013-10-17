# coding=utf-8
## "A time-tracker built with the user in mind" (where I am the user)

## Started: 10/16/13

'''
    #####################
    #   Sample Usages:  #
    #####################

    # list tasks, with call-numbers for each
    $ mcl ls

    # list group’s tasks, with call-numbers for each
    $ mcl ls --group NN

    # ls, but with time totals, start dates, due dates, etc
    $ mcl ll

    # add task if it doesn’t exist
    $ mcl add --group NN --task HW#3 --duedate 10/23

    # add group if it doesn’t exist
    $ mcl add --group Alg

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

    # print time for current task this (block, day, total)
    $ mcl show

    # pos-hoc add time a task was done
    $ mcl did NN HW#3 10:30 15:15
'''

import subprocess # this is what you're supposed to use now instead of "import sys"
# http://docs.python.org/2/library/subprocess.html#replacing-older-functions-with-the-subprocess-module
import sys  # though I don't see a way to get the command line args from subprocess
import applescripts
import os
import datetime

# example usage
applescripts.createEvent(calName='theCalName',
             eventTitle='theEventTitle',
             eventNotes='theEventNotes',
             eventLocation='theEventLocation',
             startDate='November 4, 2013 6:30:00 PM',
             endDate='November 5, 2013 1:00:00 AM')


def ls(directory=''):
    '''
    list tasks, with call-numbers for each
    in a tree-like format so the groups are displayed
    '''
    # TODO use the correct directory to `tree` in
    dir_tree = subprocess.check_output(['tree']).splitlines()
    for line in dir_tree:
        UP_TO_DOT = line.find('.')
        print line[:UP_TO_DOT]
    return len(dir_tree)


def ll(directory=''):
    '''
    ls, but with time totals, start dates, due dates, etc
    '''
    # do some counting over the CSV file
    # e.g. SUM(time) GROUPBY(subject,title)
    pass


def addGroup(name):
    '''
    if it doesn't exist, add a new folder in the current directory
    otw do nothing
    '''
    path = '.'
    file_path = path + '/' + name
    if not os.path.exists(file_path):
        os.makedirs(file_path)


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


def begin(number=None, group=None, name=None):
    '''
    "start the clock" for the given task
    this is really just printing a piece of the CSV line out, I think
        i.e. the name and start time,
             but not the endTime or block time etc.
    '''
    pass


def main(argv):
    for arg in argv:
        print arg
    ls()
    addTask('Homework Assns', 'NN Assn4', note='Nothing Here')


if __name__ == "__main__":
    main(sys.argv[1:])
