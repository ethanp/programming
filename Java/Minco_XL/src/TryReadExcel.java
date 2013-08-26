import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.*;

/**
 * 8/26/13
 * Read contents of .xlsx file and print each cell to console
 * WORKS
 */
public class TryReadExcel
{
    public static void main(String[] args) throws IOException, InvalidFormatException {
        InputStream inp = new FileInputStream("workbook.xlsx");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheetAt(0);
        for (Row row : sheet)
            for (Cell cell : row)
                System.out.println(cell);

        /*
        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
        wb.write(fileOut);
        fileOut.close();
        */
    }
}
