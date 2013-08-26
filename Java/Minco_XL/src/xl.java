import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
 */
public class xl
{
    public static void main(String[] args) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream("SpringCopy.xlsx");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        int rowEnd = Math.max(400, sheet.getLastRowNum());
        for (int rowNum = rowEnd; rowNum > 0; rowNum--) {

        }

        // Write the output back out (maybe should move the previous version for safe-keeping)
        FileOutputStream fileOut = new FileOutputStream("SpringCopy2.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
}
