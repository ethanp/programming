import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Ethan Petuchowski
 * 8/26/13
 *
 * Read in Minco Weekly Summaries
 * Summarize them
 * Insert the data into the appropriate place in the Fall_13.xls file
 *
 * Notes:
 * The first Row ("1") is number 0
 * The first Col ("A") is number 0
 */

/** TODO
 * Summarize Minco Weekly Summary
 *   Make it find the two most time-intensive activities PER subject
 *     and throw truncated versions into the activity slots in the XL
 *
 * Parse Args for Date Specs
 */
public class xl
{
    public static void main(String[] args)
            throws IOException, InvalidFormatException, ParseException {

        Semester s = new Semester();

        /* read and calculate Minco Weekly Summary */

        // skip headers
        if (s.csv.readLine() == null) {
            System.out.println("This Minco File is empty or missing or something");
            System.exit(1);
        }

        String line;
        while ((line = s.csv.readLine()) != null) {
            String[] splitLine = line.replaceAll("\"", "").replaceAll("\u0000", "").split(",");
            System.out.println(
                splitLine[s.mincoLine.get("Date")] + " " +
                splitLine[s.mincoLine.get("Title")]);

            // parse the date
            String lineDateString = splitLine[s.mincoLine.get("Date")];

            // TODO this is no longer what these will look like (works for tests though)
            DateFormat currentMincoFormat = new SimpleDateFormat("M/dd/yy");
            Date res = currentMincoFormat.parse(lineDateString);

            if (DateUtils.isSameDay(res, s.dateObjICareAbout))
                System.out.println("it's dateICareAbout");
            else System.out.println("it ain't dateICareAbout");

            // TODO this should all only be done inside the above if (condition)
            // first word of title
            String lineSubject = splitLine[s.mincoLine.get("Title")].split("\\s+")[0];

            System.out.println(lineSubject);
            if (s.subjectTotals.containsKey(lineSubject)) {
                updateSubjectTotals(s, splitLine, lineSubject);
            }
        }

        printSubjectTotals(s);


        /* READ AND UPDATE XL FILE */
        setTodayTotalRowNums(s);
        findSubjectColumns(s);

        // TODO the meat goes here...

        s.writeOut();
    }

    private static void findSubjectColumns(Semester s) {
        for (String subject : s.subjectsArray) {
            // find column that contains this subject
            for (Cell header : s.headers)
                if (header.getStringCellValue().equals(subject))
                    s.subjectColumns.put(subject, header.getColumnIndex() - 1);
        }
    }

    private static void updateSubjectTotals(Semester s, String[] splitLine, String lineSubject) {
        String thisTimeString = splitLine[s.mincoLine.get("Minutes")];
        int thisTime = Integer.parseInt(thisTimeString);
        int oldTotal = s.subjectTotals.get(lineSubject);
        s.subjectTotals.put(lineSubject, oldTotal + thisTime);
    }

    private static void printSubjectTotals(Semester s) {
        for (String st : s.subjectTotals.keySet()) {
            System.out.println(st + " => " + s.subjectTotals.get(st));
        }
    }

    private static void debugSetRowNums(Semester s) {
        System.out.println(s.lastRowNum);
        if (!s.todayFound)
            System.out.println("Today's date was not found");
        else System.out.println("Today was on row " + s.todayRowNum);
    }

    private static void setTodayTotalRowNums(Semester s) {
        for (Row row : s.sheet) {
            // skip header row
            if (row.getRowNum() == 0) continue;
            Cell dateCell = row.getCell(0);
            if (dateCell != null) {
                dateCell.setCellType(Cell.CELL_TYPE_NUMERIC); // necessary for getDate
                Date thisDate = dateCell.getDateCellValue();
                String dateString = s.newMincoDateFormat.format(thisDate);
                System.out.println(dateString);
                s.lastRowNum++;
                if (dateString.equalsIgnoreCase(s.dateICareAbout)) {
                    System.out.println("Found Today's Date");
                    s.todayFound = true;
                }
                if (!s.todayFound)
                    s.todayRowNum++;
            }
        }
    }
}
