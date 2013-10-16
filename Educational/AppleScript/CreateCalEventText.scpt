set calendarName to "To Do"
set theSummary to "Event Title"
set theDescrption to "The notes for the event"
set theLocation to "Karl's House"
set startDate to "November 4, 2013 6:30:00 PM"
set endDate to "November 5, 2013 1:00:00 AM"


set startDate to date startDate
set endDate to date endDate

tell application "Calendar"
	tell (first calendar whose name is calendarName)
		make new event at end of events with properties {summary:theSummary, start date:startDate, end date:endDate, description:theDescrption, location:theLocation}
	end tell
end tell
