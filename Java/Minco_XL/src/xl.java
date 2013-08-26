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
        InputStream inp = new FileInputStream("SpringCopy.xlsm");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheet("MainSheet");
        int lastRowNum = 0;
        for (Row row : sheet) {
            Cell dateCell = row.getCell(0);
            if (dateCell != null) {
                System.out.println(dateCell);
                lastRowNum++;
            }
        }
        System.out.println(lastRowNum);
        for (int rowNum = lastRowNum; rowNum > 0; rowNum--) {
            Row row = sheet.getRow(rowNum);
            Cell cell = row.getCell(2);
            System.out.println(cell);
        }

        /*
        // Write the output back out (maybe should move the previous version for safe-keeping)
        FileOutputStream fileOut = new FileOutputStream("SpringCopy2.xlsm");
        wb.write(fileOut);
        fileOut.close();
        */
    }
}
