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

from subprocess import call # this is what you're supposed to use now instead of "import sys"
# http://docs.python.org/2/library/subprocess.html#replacing-older-functions-with-the-subprocess-module

def createEvent(**kwargs):
    print kwargs
    theScriptHeader='''set calendarName to "{calName}"
set theSummary to "{eventTitle}"
set theDescription to "{eventNotes}"
set theLocation to "{eventLocation}"
set startDate to "{startDate}"
set endDate to "{endDate}"
'''

    restOfTheScript='''
    set startDate to date startDate
set endDate to date endDate

tell application "Calendar"
	tell (first calendar whose name is calendarName)
		make new event at end of events with properties {summary:theSummary, start date:startDate, end date:endDate, description:theDescription, location:theLocation}
	end tell
end tell'''
    theScriptHeader = theScriptHeader.format(**kwargs)
    print theScriptHeader+restOfTheScript

createEvent(calName='theCalName',
             eventTitle='theEventTitle',
             eventNotes='theEventNotes',
             eventLocation='theEventLocation',
             startDate='November 4, 2013 6:30:00 PM',
             endDate='November 5, 2013 1:00:00 AM')