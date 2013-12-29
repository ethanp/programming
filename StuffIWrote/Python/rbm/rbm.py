# coding=utf-8
## A command line tool to make understanding what I do all day simpler
## Started: 12/28/13

import sys, datetime, argparse
import applescripts

# set home dir
HOME_PATH = '/Users/Ethan/rbm_file'
STORAGE_FILE = open(HOME_PATH, 'a')
TIME_FORMAT = '%m/%d/%Y %I:%M %p'
TIME_NOW = datetime.datetime.now()
DIFFERENCE_FORMAT = '%H:%M:%s'

# tested to work
def start(name=None):
    '''
    "start the clock" for the given task
    create a 1-hour event for it in the epet@icloud calendar "Work"
    '''
    if not name:
        print "no name given, cancelling"
        return False

    # print name of task and timestamp
    STORAGE_FILE.write(name+'\n')
    STORAGE_FILE.write(TIME_NOW.strftime(TIME_FORMAT)+'\n')

    # create Calendar event
    END_TIME = TIME_NOW + datetime.timedelta(hours=1)
    applescripts.createEvent(calName='Work',
                             eventTitle=name,
                             eventNotes='',
                             eventLocation='',
                             startDate=TIME_NOW.strftime(TIME_FORMAT),
                             endDate=END_TIME.strftime(TIME_FORMAT))


# seems to work
def end():
    '''
    "stop the clock" for the running task
    adjust the Calendar event to properly surround it
    print the time spent
    '''
    name_line, date_line = show()
    applescripts.editEvent(calName='Work',
                           eventTitle=name_line,
                           eventNotes='',
                           eventLocation='',
                           startDate=date_line,
                           endDate=TIME_NOW.strftime(TIME_FORMAT))


def show():
    '''
    print time spent so far on current task
    '''
    with open(HOME_PATH, 'rb') as read_file:
        lines = read_file.readlines()
        name_line = lines[-2].strip()
        date_line = lines[-1].strip()
    start_time = datetime.datetime.strptime(date_line, TIME_FORMAT)
    time_difference = TIME_NOW - start_time
    print 'Total Time for %s: %s' % (name_line, str(time_difference)[:4])
    return name_line, date_line


# TODO
def cancel():
    '''
    cancel current task
    '''
    # remove the calendar event
    # that's it
    pass


def main(argv):
    if not argv:
        print 'either start <task>, or end, or show'
        exit(1)
    if argv[0] == 'start': start(' '.join(argv[1:]))
    if argv[0] == 'end':   end()
    if argv[0] == 'show':  show()


if __name__ == "__main__":
    main(sys.argv[1:])
