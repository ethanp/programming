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
 * Parse Args for Date Specs
 *
 */
public class xl
{
    public static void main(String[] args) throws IOException, InvalidFormatException {


        /* initializations */

        // subject to change
        String[] subjectsArray = {"Arch", "AI", "OS", "C++"};
        String csvFile = "Minco Week 5.csv";  // week# can be easily obtained from the Date object
        // /Users/Ethan/Library/Application\ Support/Minco/CSV_Files/Documents/Minco/2013/Minco\ Week\ 5.csv

        // might eventually change
        String excelFile = "SpringCopy.xlsm";
        Map<String, Integer> mincoLine = new HashMap<>(5);
        mincoLine.put("Date", 0);
        mincoLine.put("Minutes", 3);
        mincoLine.put("Title", 4);

        // not subject to change
        int lastRowNum, todayRowNum, osColNum;
        lastRowNum = todayRowNum = osColNum = 0;
        Boolean todayFound = false;
        String line;
        List<String> subjects = Arrays.asList(subjectsArray);
        Map<String, Integer> subjectTotals = new HashMap<>(subjectsArray.length);
        for (String subject : subjects) {
            subjectTotals.put(subject, 0);
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // bc of minco dates
        String today = dateFormat.format(new Date());




        /* read and calculate Minco Weekly Summary */

        BufferedReader csv = new BufferedReader(new FileReader(csvFile));

        // skip headers
        if (csv.readLine() == null) {
            System.out.println("This Minco File is empty or missing or something");
            System.exit(1);
        }

        while ((line = csv.readLine()) != null) {
            String[] splitLine = line.replaceAll("\"", "").replaceAll("\u0000", "").split(",");
            System.out.println(splitLine[mincoLine.get("Date")]+" "+splitLine[mincoLine.get("Title")]);

            // only use the correct date
            if (){};

            // first word of title
            String thisSubject = splitLine[mincoLine.get("Title")].split("\\s+")[0];

            System.out.println(thisSubject);
            if (subjectTotals.containsKey(thisSubject)) {
                String thisTimeString = splitLine[mincoLine.get("Minutes")];
                int thisTime = Integer.parseInt(thisTimeString);
                int oldTotal = subjectTotals.get(thisSubject);
                subjectTotals.put(thisSubject, oldTotal + thisTime);
            }
        }

        for (String s : subjectTotals.keySet()) {
            System.out.println(s+" => "+subjectTotals.get(s));
        }




        /* read and update XL File */

        // open the file and worksheet
        InputStream inputStream = new FileInputStream(excelFile);
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheet("MainSheet");

        // find last row number & today's row number
        for (Row row : sheet) {

            // skip header row
            if (row.getRowNum() == 0) continue;

            Cell dateCell = row.getCell(0);
            if (dateCell != null) {
                dateCell.setCellType(Cell.CELL_TYPE_NUMERIC); // necessary for getDate
                Date thisDate = dateCell.getDateCellValue();
                String dateString = dateFormat.format(thisDate);
                System.out.println(dateString);
                lastRowNum++;

                if (dateString.equalsIgnoreCase(today)) {
                    System.out.println("Found Today's Date");
                    todayFound = true;
                }

                if (!todayFound)
                    todayRowNum++;
            }
        }

        // print findings
        /*
        System.out.println(lastRowNum);
        if (!todayFound)
            System.out.println("Today's date was not found");
        else System.out.println("Today was on row " + todayRowNum);
        */

        // FILLER CODE: print column "C" values backwards, excluding header-row
        /*
        for (int rowNum = lastRowNum; rowNum > 0; rowNum--) {
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(2);
            System.out.println(cell);
        }
        */

        // find column that contains Operating Systems' times
        //   using this sheet probably means I should switch to having a single column
        //     holding the total-times per-subject
        //   but I can still have multiple descriptor-cells for specific activities
        Row headers = sheet.getRow(0);
        for (Cell header : headers)
            if (header.getStringCellValue().equals("439 O-Sys's"))
                osColNum = header.getColumnIndex() - 1;
        System.out.println(osColNum);

        // Write the output back out (maybe should move the previous version for safe-keeping)
        /*
        FileOutputStream fileOut = new FileOutputStream("SpringCopy2.xlsm");
        wb.write(fileOut);
        fileOut.close();
        */
    }
}
