/**
 * 8/26/13
 * This code is copy-pasted from
 * http://poi.apache.org/spreadsheet/quick-guide.html
 * in the section "Creating Cells"
 * WORKS
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class TryCreateNewExcel
{
    public static void main(String[] args) throws IOException {
        Workbook wb = new XSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short)0);
        // Create a cell and put a value in it.
        Cell cell = row.createCell(0);
        cell.setCellValue(1);

        // Or do it on one line.
        row.createCell(1).setCellValue(1.2);
        row.createCell(2).setCellValue(
                createHelper.createRichTextString("This is a string"));
        row.createCell(3).setCellValue(true);

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
}
