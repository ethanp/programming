import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ethan Petuchowski
 * 8/26/13
 *
 * NOTES:
 * - week# can be easily obtained from the Date object
 * - Minco files can be found at: /Users/Ethan/Library/Application\
 *      Support/Minco/CSV_Files/Documents/Minco/2013/Minco\ Week\ 5.csv
 */
public class Semester
{
    int            lastRowNum, theDayRowNum, osColNum;
    Row            headers;
    Date           dateObjICareAbout;
    Sheet          sheet;
    String         dateICareAbout;
    Workbook       workbook;
    BufferedReader csv;

    String[]                          subjectsArray      = {"Arch", "AI", "OS", "C++"};
    List<String>                      subjects           = Arrays.asList(subjectsArray);
    Map<String, Integer>              mincoLine          = new HashMap<>(3);
    Map<String, Map<String, Integer>> subjectTaskTotals  = new HashMap<>();
    Map<String, Integer>              subjectTotals      = new HashMap<>();
    Map<String, List<String>>         subjectTasks       = new HashMap<>();
    Map<String, Integer>              subjectColumns     = new HashMap<>();
    String                            csvFile            = "Minco Week 5.csv"; // TODO make dynamic
    String                            excelName          = "SpringCopy";
    String                            excelFile          = excelName + ".xlsm";
    String                            backupFile         = excelName + "Back.xlsm";
    DateFormat                        newMincoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Boolean                           todayFound         = false;


    Semester() throws ParseException, FileNotFoundException {
        workbook = null;
        mincoLine.put("Date", 0);
        mincoLine.put("Minutes", 3);
        mincoLine.put("Title", 4);
        lastRowNum = theDayRowNum = osColNum = 0;
        for (String subject : subjects)
            subjectTaskTotals.put(subject, new HashMap<String, Integer>());
        dateObjICareAbout = newMincoDateFormat.parse("2013-01-30"); // TODO use input params
        dateICareAbout = newMincoDateFormat.format(dateObjICareAbout);
        sheet = this.getSheet(excelFile);
        headers = sheet.getRow(0);
        csv = new BufferedReader(new FileReader(csvFile));
    }

    private Sheet getSheet(String excelFile) {
        InputStream inputStream = null;
        try { inputStream = new FileInputStream(excelFile); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        try { workbook = WorkbookFactory.create(inputStream); }
        catch (IOException | InvalidFormatException e) { e.printStackTrace(); }
        assert workbook != null;

        return workbook.getSheet("MainSheet");
    }

    public void writeOut() {
        FileOutputStream fileOut = null;
        try { fileOut = new FileOutputStream(backupFile); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        try { workbook.write(fileOut); }
        catch (IOException e) { e.printStackTrace(); }

        try { fileOut.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
