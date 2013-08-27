import sun.util.calendar.LocalGregorianCalendar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** Ethan Petuchowski 8/26/13 */
public class Semester
{
    // subject to change
    String[]             subjectsArray = {"Arch", "AI", "OS", "C++"};
    List<String>         subjects      = Arrays.asList(subjectsArray);
    Map<String, Integer> mincoLine     = new HashMap<>(3);
    Map<String, Integer> subjectTotals = new HashMap<>(subjectsArray.length);

    // week# can be easily obtained from the Date object
    String               csvFile       = "Minco Week 5.csv";
    // /Users/Ethan/Library/Application\ Support/Minco/CSV_Files/Documents/Minco/2013/Minco\ Week\ 5.csv

    // probable new minco date format, may as well use it throughout
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    Date   dateObjICareAbout;
    String dateICareAbout;
    // might eventually change
    String excelFile = "SpringCopy.xlsm";

    // not subject to change
    int lastRowNum, todayRowNum, osColNum;
    Boolean      todayFound = false;

    Semester() throws ParseException {
        mincoLine.put("Date", 0);
        mincoLine.put("Minutes", 3);
        mincoLine.put("Title", 4);
        lastRowNum = todayRowNum = osColNum = 0;
        for(String subject:subjects)subjectTotals.put(subject,0); // create counter

        // TODO this is for testing, and should eventually be set by the args[]
        dateObjICareAbout = dateFormat.parse("2013-01-26");
        dateICareAbout = dateFormat.format(dateObjICareAbout);
    }

}
