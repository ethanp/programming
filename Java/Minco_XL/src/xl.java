import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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


        /* initializations */
        String line;
        Semester s = new Semester();

        /* read and calculate Minco Weekly Summary */

        BufferedReader csv = new BufferedReader(new FileReader(s.csvFile));

        // skip headers
        if (csv.readLine() == null) {
            System.out.println("This Minco File is empty or missing or something");
            System.exit(1);
        }

        while ((line = csv.readLine()) != null) {
            String[] splitLine = line.replaceAll("\"", "").replaceAll("\u0000", "").split(",");
            System.out.println(
                    splitLine[s.mincoLine.get("Date")]
                    + " "
                    + splitLine[s.mincoLine.get("Title")]);

            // parse the date
            String lineDateString = splitLine[s.mincoLine.get("Date")];
            // TODO this is no longer what these will look like (works for tests though)
            DateFormat currentMincoFormat = new SimpleDateFormat("M/dd/yy");
            Date res = currentMincoFormat.parse(lineDateString);

            // TODO use the correct date (not necessarily 'dateICareAbout')
            if (DateUtils.isSameDay(res, s.dateObjICareAbout))
                System.out.println("it's dateICareAbout");
            else System.out.println("it ain't dateICareAbout");

            // TODO this should all only be done inside the above if (condition)
            // first word of title
            String lineSubject = splitLine[s.mincoLine.get("Title")].split("\\s+")[0];

            System.out.println(lineSubject);
            if (s.subjectTotals.containsKey(lineSubject)) {
                String thisTimeString = splitLine[s.mincoLine.get("Minutes")];
                int thisTime = Integer.parseInt(thisTimeString);
                int oldTotal = s.subjectTotals.get(lineSubject);
                s.subjectTotals.put(lineSubject, oldTotal + thisTime);
            }
        }

        for (String st : s.subjectTotals.keySet()) {
            System.out.println(st + " => " + s.subjectTotals.get(st));
        }



        /* read and update XL File */

        // open the file and worksheet
        Sheet sheet = getSheet(s.excelFile);

        // find last row number & dateICareAbout's row number
        setTodayTotalRowNums(s, s.dateICareAbout, sheet);
//        debugSetRowNums(s);

        // sample lines
//        Row row = sheet.getRow(rowNum);
//        Cell cell = row.getCell(2);

        // set the values that need to be set
        for (String subject : s.subjectsArray) ;

        // find column that contains Operating Systems' times
        //   using this sheet probably means I should switch to having a single column
        //     holding the total-times per-subject
        //   but I can still have multiple descriptor-cells for specific activities
        Row headers = sheet.getRow(0);
        for (Cell header : headers)
            if (header.getStringCellValue().equals("439 O-Sys's"))
                s.osColNum = header.getColumnIndex() - 1;
        System.out.println(s.osColNum);

        // Write the output back out (maybe should move the previous version for safe-keeping)
        /*
        FileOutputStream fileOut = new FileOutputStream("SpringCopy2.xlsm");
        wb.write(fileOut);
        fileOut.close();
        */
    }

    private static void debugSetRowNums(Semester s) {
        System.out.println(s.lastRowNum);
        if (!s.todayFound)
            System.out.println("Today's date was not found");
        else System.out.println("Today was on row " + s.todayRowNum);
    }

    private static void setTodayTotalRowNums(Semester s, String today, Sheet sheet) {

        for (Row row : sheet) {

            // skip header row
            if (row.getRowNum() == 0) continue;

            Cell dateCell = row.getCell(0);

            if (dateCell != null) {
                dateCell.setCellType(Cell.CELL_TYPE_NUMERIC); // necessary for getDate
                Date thisDate = dateCell.getDateCellValue();
                String dateString = s.dateFormat.format(thisDate);
                System.out.println(dateString);
                s.lastRowNum++;

                if (dateString.equalsIgnoreCase(today)) {
                    System.out.println("Found Today's Date");
                    s.todayFound = true;
                }

                if (!s.todayFound)
                    s.todayRowNum++;
            }
        }
    }

    private static Sheet getSheet(String excelFile) throws IOException, InvalidFormatException {
        InputStream inputStream = new FileInputStream(excelFile);
        Workbook workbook = WorkbookFactory.create(inputStream);
        return workbook.getSheet("MainSheet");
    }
}
