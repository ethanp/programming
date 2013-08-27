import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Ethan Petuchowski
 * 8/26/13
 *
 * NOTES:
 *
 *  week# can be easily obtained from the Date object
 *
 *  Minco files can be found at:
 *      /Users/Ethan/Library/Application\ Support/Minco/CSV_Files/Documents/Minco/2013/Minco\
 *      Week\ 5.csv
 */
public class Semester
{
    Date   dateObjICareAbout;
    String dateICareAbout;
    BufferedReader csv;
    int lastRowNum, todayRowNum, osColNum;
    Row headers;
    Workbook workbook;

    Sheet sheet;
    String[]             subjectsArray      = {"Arch", "AI", "OS", "C++"};
    List<String>         subjects           = Arrays.asList(subjectsArray);
    Map<String, Integer> mincoLine          = new HashMap<>(3);
    Map<String, Integer> subjectTotals      = new HashMap<>(subjectsArray.length);
    Map<String, Integer> subjectColumns      = new HashMap<>(subjectsArray.length);
    String               csvFile            = "Minco Week 5.csv";
    String               excelName          = "SpringCopy";
    String               excelFile          = excelName + ".xlsm";
    String               backupFile         = excelName + "Back.xlsm";
    DateFormat           newMincoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Boolean              todayFound         = false;


    Semester() throws ParseException, FileNotFoundException {
        workbook = null;
        mincoLine.put("Date", 0);
        mincoLine.put("Minutes", 3);
        mincoLine.put("Title", 4);
        lastRowNum = todayRowNum = osColNum = 0;
        for (String subject : subjects) subjectTotals.put(subject, 0); // create counter

        dateObjICareAbout = newMincoDateFormat.parse("2013-01-26"); // TODO use input params
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
        try { fileOut = new FileOutputStream(excelName + "out.xlsx"); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        try { workbook.write(fileOut); }
        catch (IOException e) { e.printStackTrace(); }

        try { fileOut.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
