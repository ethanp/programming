## 10/16/13
## print an AppleScript to a file, then execute it

# note: once you write the file as text, you have to call `osacompile theFile` on it
# you might have to make sure the permissions on it are properly set
#    I don't know what the Python default is to do
# then you call `osascript theFile` on it to execute it

from subprocess import * # this is what you're supposed to use now instead of "import sys"
# http://docs.python.org/2/library/subprocess.html#replacing-older-functions-with-the-subprocess-module

theScript='''set calendarName to "To Do"
set theSummary to "Event Title"
set theDescription to "The notes for the event"
set theLocation to "Karl's House"
set startDate to "November 4, 2013 6:30:00 PM"
set endDate to "November 5, 2013 1:00:00 AM"


set startDate to date startDate
set endDate to date endDate

tell application "Calendar"
	tell (first calendar whose name is calendarName)
		make new event at end of events with properties {summary:theSummary, start date:startDate, end date:endDate, description:theDescription, location:theLocation}
	end tell
end tell'''
with open("script.scpt", 'wr') as script:
    script.write(theScript)

call(["osacompile", "script.scpt"])
call(["osascript", "script.scpt"])