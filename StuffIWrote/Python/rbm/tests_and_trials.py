from rbm import *
from subprocess import call

def test_createEvent_date_format():
    applescripts.create_event(calName='Work',
                             eventTitle='Sample Event',
                             eventNotes='',
                             eventLocation='',
                             startDate='12/30/2013 6:20:00 PM',
                             endDate='12/30/2013 10:20:00 PM')

def test_blank_start():
    if start():  print 'start() should have returned true'
    else: print 'blank start() passed'

def test_start():
    start('Insert a Silly Name Here')

if __name__ == "__main__":
    test_start()
