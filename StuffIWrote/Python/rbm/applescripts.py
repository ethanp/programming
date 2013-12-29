# Copied from the unfinished Minco Command Line Tool project

def createEvent(**kwargs):
    """
    Expected Keyword Arguments:
        calName
        eventTitle
        eventNotes
        eventLocation
        startDate
        endDate
    """
    print kwargs
    theScriptHeader='''
set calendarName to "{calName}"
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
end tell
    '''

    theScriptHeader = theScriptHeader.format(**kwargs)
    return theScriptHeader+restOfTheScript


def editEvent(**kwargs):
    # TODO you should only have to pass in the parameters you want to change
    theScriptHeader='''
set calendarName to "{calName}"
set theSummary to {eventTitle}
set theDescription to {eventNotes}
set theLocation to {eventLocation}
set startDate to {startDate}
set endDate to "{endDate}"
    '''

    restOfTheScript='''
set startDate to date startDate
set endDate to date endDate
tell application "Calendar"
	tell (first calendar whose name is calendarName)
		tell (last event whose startDate is startDate)
			set start date to startDate
			set end date to endDate
			set description to theDescription
			set summary to theSummary
			set location to theLocation
		end tell
	end tell
end tell '
    '''

    theScriptHeader = theScriptHeader.format(**kwargs)
    return theScriptHeader+restOfTheScript


def createReminder(**kwargs):
    '''
    Expected Keyword Arguments
        todoList
        eventTitle
        notes
        dueDate
    '''
    print kwargs
    theScriptHeader='''
set theList to {todoList}
set theTitle to {eventTitle}
set theNote to {note}
set theDueDate to {dueDate}
    '''

    restOfTheScript = '''
set theDueDate to date theDueDate

tell application "Reminders"
	tell (first list whose name is theList)
		make new reminder at end of reminders with properties {name:theTitle, body:theNote, due date:theDueDate}
	end tell
end tell
    '''

    theScriptHeader = theScriptHeader.format(**kwargs)
    return theScriptHeader+restOfTheScript


def editReminder(**kwargs):
    '''
    Expected Keyword Arguments
        todoList
        eventTitle
        notes
        dueDate
    '''
    print kwargs
    theScriptHeader='''
set theList to {todoList}
set theTitle to {eventTitle}
set theNote to {note}
set theDueDate to {dueDate}
    '''

    restOfTheScript = '''
set theDueDate to date theDueDate
tell application "Reminders"
	tell (first list whose name is theList)
		set name to theTitle
		set body to theNote
		set due date to theDueDate
	end tell
end tell
    '''
    theScriptHeader = theScriptHeader.format(**kwargs)
    return theScriptHeader + restOfTheScript
