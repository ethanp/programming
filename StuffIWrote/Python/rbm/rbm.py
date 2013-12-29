# coding=utf-8
## A command line tool to make understanding what I do all day simpler
## Started: 12/28/13

import shutil
import subprocess
import sys
import os
import datetime
import glob
import csv
import argparse

import applescripts

# set home dir
HOME_PATH = '/Users/Ethan/rbm_file'
storage_file = open(HOME_PATH, 'a')

# TODO
def start(name=None):
    '''
    "start the clock" for the given task
    '''
    if not name:
        print "no name given, cancelling"
        return False

    # print name of task
    storage_file.write(name)

    # print timestamp
    t = datetime.datetime
    now_string = str(t.hour) + ':' + str(t.minute) + ':' + str(t.second)
    storage_file.write(now_string)

    # create Calendar event
    applescripts.createEvent(calName='Work',
                             eventTitle=name,
                             eventNotes='',
                             startDate='')

    pass

# example usage
#applescripts.createEvent(calName='theCalName',
#             eventTitle='theEventTitle',
#             eventNotes='theEventNotes',
#             eventLocation='theEventLocation',
#             startDate='November 4, 2013 6:30:00 PM',
#             endDate='November 5, 2013 1:00:00 AM')


# TODO
def end():
    '''
    "stop the clock" for the running task
    '''
    # open the storage file
    # find the name of the running task
    # find the timestamp
    # print the total time spent
    # adjust Calendar event (might take a little while, not sure why though)
    pass


# TODO
def show():
    '''
    print time for current task
    '''
    # open the storage file
    # find the timestamp
    # calc time since timestamp
    # print time since timestamp
    # NOTE does a lot of the same things as end, so they could probably share a subroutine
    pass


# TODO
def cancel():
    '''
    cancel current task
    '''
    # remove the calendar event
    # that's it
    pass


# TODO these are in order of preference of implementation
def create_command_line_options():
    options = argparse.ArgumentParser(description="Command Line Tool for Tracking Time in Apple's `Calendar`")
    options.add_argument('start', help='create 1-hour event of given name')
    options.add_argument('end', help='stop current running task')
    options.add_argument('move', help='edit start time of current running task')
    options.add_argument('show', help='print time so far spent on current event')
    options.add_argument('cancel', help='cancel current event')
    options.add_argument('ls', help='list groups')
    options.add_argument('add', help='create new task-group')
    options.add_argument('delete', help='delete group')


def main(argv):
    create_command_line_options()


if __name__ == "__main__":
    main(sys.argv[1:])
