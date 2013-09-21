import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

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

/** TODO use Guava? */
public class xl
{
    public static void main(String[] args) throws Exception {

        Semester s = new Semester(args);

        /* read and calculate Minco Weekly Summary */
        readMincoLog(s);
        printActivityTimes(s);
        calcTopTwoSubjectTasks(s);

        /* READ AND UPDATE XL FILE */
        setDayRowNum(s);
        locateSubjectColumns(s);
        fillInData(s);
        s.writeOut();
    }

    private static void printActivityTimes(Semester s) {
        String divider = "===============";
        System.out.println("\n" + divider);
        for (Subject subject : s.subjects.values())
            if (subject.hasTasks())
                subject.printTaskTimes(divider);
    }

    private static void readMincoLog(Semester s) throws IOException, ParseException {
        skipheaders(s);
        String line;
        while ((line = s.csv.readLine()) != null) {
            if (line.equals("\u0000")) continue;
            String[] splitLine = line.replaceAll("\"", "").replaceAll("\u0000", "").split(",");
            Date res = makeDate(s, splitLine);
            if (DateUtils.isSameDay(res, s.dateObjICareAbout)) {
                String[] taskLine  = splitLine[s.mincoLine.get("Title")].split("\\s+");
                String lineSubject = taskLine[0];
                String lineTask    = getLineTask(taskLine);
                int    lineTime    = getLineTime(s, splitLine);

                // valid subject name, add time to subject
                if (s.subjects.containsKey(lineSubject))
                    s.subjects.get(lineSubject).addToTask(lineTask, lineTime);

                // not a subject, print anyway
                else System.out.printf("Other: %s %s %d\n", lineSubject, lineTask, lineTime);
            }
        }
    }

    /*
     * on the excel sheet, for each subject, write the total time spent, and top two tasks
     */
    private static void fillInData(Semester s) {
        double hwTime = 0;
        for (Subject subject : s.subjects.values()) {
            int col = subject.getColumn();
            Row row = s.sheet.getRow(s.theDayRowNum);
            if (s.debug)
                System.out.println("\nWriting Subject: " + subject);

            // total time
            double timeInHours = subject.timeInHours();
            hwTime += timeInHours;
            row.getCell(col).setCellValue(timeInHours);
            if (s.debug)
                printNewCellContent(s.theDayRowNum, col, subject.timeString());

            // print tasks (don't overwrite what's there if there's nothing to write)
            if (subject.hasTasks()) {
                row.getCell(++col).setCellValue(subject.biggest());
                if (s.debug)
                    printNewCellContent(s.theDayRowNum, col, subject.biggest());
                if (subject.needsTwoColumns()) {
                    row.getCell(++col).setCellValue(subject.secondBiggest());
                    if (s.debug)
                        printNewCellContent(s.theDayRowNum, col, subject.secondBiggest());
                }
            }
        }
        System.out.printf("\nTotal Time: %.2f\n", hwTime);
    }

    /*
     * "Putting Book in (15, AG)"
     */
    private static void printNewCellContent(int row, int col, String val) {
        if (!val.equals("")) {
            String colString = CellReference.convertNumToColString(col);
            System.out.printf("Putting %s in (%d, %s)\n", val, row + 1, colString);
        }
    }

    private static int getLineTime(Semester s, String[] splitLine) {
        String lineTimeString = splitLine[s.mincoLine.get("Minutes")];
        return Integer.parseInt(lineTimeString);
    }

    private static String getLineTask(String[] taskLine) {
        StringBuilder lineTaskB = new StringBuilder();
        for (int i = 1; i < taskLine.length; i++)
            lineTaskB.append(taskLine[i] + " ");
        return lineTaskB.toString().trim();
    }

    private static Date makeDate(Semester s, String[] splitLine) throws ParseException {
        return s.newMincoDateFormat.parse(splitLine[s.mincoLine.get("Date")]);
    }

    private static void printDateAndTitle(Semester s, String[] splitLine) {
        System.out.println(
            splitLine[s.mincoLine.get("Date")] + " " +
            splitLine[s.mincoLine.get("Title")]);
    }

    private static void skipheaders(Semester s) throws IOException {
        if (s.csv.readLine() == null) {
            System.out.println("This Minco File is empty or missing or something");
            System.exit(1);
        }
    }

    /*
     * for each subject, find the column index of the place to put the Total Time
     */
    private static void locateSubjectColumns(Semester s) {
        for (Subject subject : s.subjects.values()) {
            for (Cell header : s.headers)
                if (header.getStringCellValue().equals("c"+subject.toString()))
                    subject.setColumn(header.getColumnIndex() - 1);
        }
    }

    /*
     * for each subject, find the two tasks with most time spent on them
     */
    private static void calcTopTwoSubjectTasks(Semester s) {
        for (Subject subject : s.subjects.values())
            subject.calcTopTwo();
    }

    /*
     * Set the index of today's row in this sheet, and
     * Set the index of the last row in this sheet
     */
    private static void setDayRowNum(Semester s) {
        boolean foundIt = false;
        for (Row row : s.sheet) {
            if (row.getRowNum() == 0) continue;  // skip header row
            Cell dateCell = row.getCell(0);
            if (dateCell != null) {
                dateCell.setCellType(Cell.CELL_TYPE_NUMERIC); // necessary for getDate
                Date thisDate = dateCell.getDateCellValue();
                String dateString = s.newMincoDateFormat.format(thisDate);
                s.theDayRowNum++;
                if (dateString.equals(s.dateICareAbout)) {
                    foundIt = true;
                    break;
                }
            }
        }
        if (!foundIt) {
            System.out.println("\nRequested date wasn't found in XL sheet");
            System.exit(2);
        }
        else if (s.debug) System.out.println("\nDate was on row " + s.theDayRowNum);
    }
}
