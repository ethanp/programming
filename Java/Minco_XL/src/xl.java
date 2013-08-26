import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
public class xl
{
    public static void main(String[] args) throws IOException, InvalidFormatException {
        int lastRowNum, todayRowNum, osColNum;
        lastRowNum = todayRowNum = osColNum = 0;
        Boolean todayFound  = false;

        // open the file and worksheet
        InputStream inp = new FileInputStream("SpringCopy.xlsm");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheet("MainSheet");

        // this is how we'll format dates
        SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy");

        // get the current date
        Calendar currentDate = Calendar.getInstance();
        String today = formatter.format(currentDate.getTime());
        System.out.println(today);

        // find last row number & today's row number
        for (Row row : sheet) {

            // skip header row
            if (row.getRowNum() == 0) continue;

            Cell dateCell = row.getCell(0);
            if (dateCell != null) {
                dateCell.setCellType(Cell.CELL_TYPE_NUMERIC); // this was necessary for date (?)
                Date date = dateCell.getDateCellValue();
                String dateString = formatter.format(date);
                System.out.println(dateString);
                lastRowNum++;

                if (dateString.equalsIgnoreCase(today))
                    todayFound = true;

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
