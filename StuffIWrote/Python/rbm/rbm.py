# coding=utf-8
## A command line tool to make understanding what I do all day simpler
## 12/28/13

import sys, datetime, applescripts

HOME_PATH = '/Users/Ethan/rbm_file'
STORAGE_FILE = open(HOME_PATH, 'a')
TIME_FORMAT = '%m/%d/%Y %I:%M %p'
DATETIME_NOW = datetime.datetime.now()
DIFFERENCE_FORMAT = '%H:%M:%s'

def start(name=None):
    '''
    note current time as start time for the given task name
    create a 1-hour event for it in the epet@icloud calendar "Work"
    '''
    if not name:
        print "no name given, cancelling"
        exit(1)

    STORAGE_FILE.write(name+'\n')
    STORAGE_FILE.write(DATETIME_NOW.strftime(TIME_FORMAT)+'\n')

    END_TIME = DATETIME_NOW + datetime.timedelta(hours=1)
    applescripts.create_event(calName='Work', eventTitle=name,
                             startDate=DATETIME_NOW.strftime(TIME_FORMAT),
                             endDate=END_TIME.strftime(TIME_FORMAT))


def end():
    '''
    print the time spent (so far) AND
    adjust the Calendar event to properly surround it
    '''
    title, start_time = show()
    applescripts.edit_event(calName='Work', eventTitle=title, startDate=start_time,
                            endDate=DATETIME_NOW.strftime(TIME_FORMAT))


def show():
    '''
    print time spent (so far) on current task
    '''
    with open(HOME_PATH, 'rb') as read_file:
        lines = read_file.readlines()
        name_line = lines[-2].strip()
        date_line = lines[-1].strip()
    start_datetime = datetime.datetime.strptime(date_line, TIME_FORMAT)
    time_difference = DATETIME_NOW - start_datetime
    print 'Time for %s: %s' % (name_line, str(time_difference)[:4])
    return name_line, date_line


def main(argv):
    if not argv:  print 'either start <task>, or end, or show'
    if argv[0] == 'start': start(' '.join(argv[1:]))
    if argv[0] == 'end':   end()
    if argv[0] == 'show':  show()


if __name__ == "__main__":
    main(sys.argv[1:])
