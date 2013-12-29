from rbm import *
from subprocess import call

def test_createEvent_date_format():
    the_script = applescripts.createEvent(calName='Work',
                                          eventTitle='Sample Event',
                                          eventNotes='',
                                          eventLocation='',
                                          startDate='12/30/2013 6:20:00 PM',
                                          endDate='12/30/2013 10:20:00 PM')

    with open("script.scpt", 'wr') as script:
        script.write(the_script)
    call(["osacompile", "script.scpt"])
    call(["osascript", "script.scpt"])
    call(["rm", "-f", "script.scpt"])

if __name__ == "__main__":
    test_createEvent_date_format()
