import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import org.apache.commons.lang3.StringUtils;

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

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;


/**
 * Ethan Petuchowski
 * 8/26/13
 *
 * NOTES:
 * - week# can be easily obtained from the Date object
 * - Minco files can be found at: /Users/Ethan/Library/Application
 *      Support/Minco/CSV_Files/Documents/Minco/2013/Minco Week 5.csv
 */
public class Semester
{
    /* declarations */
    Row            headers;
    Date           dateObjICareAbout;
    Sheet          sheet;
    String         dateICareAbout;
    String         rightNow;
    String         backupFile;
    String         csvFile;
    Workbook       workbook;
    BufferedReader csv;

    /* initializations */
    int                  theDayRowNum  = 0;
    Boolean              debug         = false;
    Map<String, Subject> subjects      = new HashMap<>();
    Map<String, Integer> mincoLine     = new HashMap<>(3);

    /* constants */
    public final int MINCOLINE_DATE = 0;
    public final int MINCOLINE_MINUTES = 3;
    public final int MINCOLINE_TITLE = 4;

    /* subject to change */
    String     excelName          = "Fall '13";
    String     xlDir              = "/Users/Ethan/Dropbox/School Help Files/";
    String     excelFile          = xlDir + excelName + ".xlsm";
    String     backupDir          = xlDir + excelName + " Backups/";
    String[]   subjectsArray      = {"Alg", "NN", "LrngThry", "Geo"};
    DateFormat newMincoDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    Semester(String[] args) throws Exception {

        /* create Map<String, Subject> subjects */
        for (String name : subjectsArray)
            subjects.put(name, new Subject(name));

        Calendar cal = getDateFromOptions(args);
        this.setXlSheet();
        this.setXlHeaders();
        getCsvFile(cal);
        doBackup();
    }

    private Calendar getDateFromOptions(String[] args) throws ParseException {
        Options options = createOptions();
        CommandLine cl = parseArgs(args, options);
        Calendar cal = setDateFromCL(cl);
        dateICareAbout = newMincoDateFormat.format(dateObjICareAbout);
        System.out.println("Looking for " + dateICareAbout);
        return cal;
    }

    private void setXlHeaders() {
        headers = sheet.getRow(0);
    }

    private void getCsvFile(Calendar cal) throws FileNotFoundException {
        int week = cal.get(Calendar.WEEK_OF_YEAR);
        csvFile = "/Users/Ethan/Library/Application Support/Minco/" +
                  "CSV_Files/Documents/Minco/2013/Minco Week "+week+".csv";
        csv = new BufferedReader(new FileReader(csvFile));
    }

    private CommandLine parseArgs(String[] args, Options options) throws ParseException {CommandLineParser cli = new GnuParser();
        return cli.parse(options, args, true);
    }

    private void doBackup() throws IOException {
        System.out.println("IT'S TOO LATE TO CANCEL UNTIL THE WHOLE THING FINISHES!");
        System.out.println("Backing Up...");
        rightNow = new SimpleDateFormat("MM-dd-HH-mm-ss").format(new Date());
        backupFile = backupDir + excelName + rightNow + ".xlsm";
        Path start = Paths.get(excelFile);
        Path end = Paths.get(backupFile);
        CopyOption[] options = new CopyOption[] {
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.COPY_ATTRIBUTES,
        };
        Files.copy(start, end, options);
    }

    private Calendar setDateFromCL(CommandLine cl) {
        Calendar cal = Calendar.getInstance();
        if (cl.hasOption("y"))
            cal.add(Calendar.DATE, -1);
        else if (cl.hasOption("d")) {    // use given date (defaults to current year)
            String enteredDate = cl.getOptionValue("d").replace('-','/');
            if (StringUtils.countMatches(enteredDate, "/") == 1)
                enteredDate += "/" + cal.get(Calendar.YEAR);
            Date d = new Date(enteredDate);
            cal.setTime(d);
        }
        if (cl.hasOption("n"))
            System.exit(3);
        if (cl.hasOption("t"))
            debug = true;
        dateObjICareAbout = cal.getTime();
        return cal;
    }

    private Options createOptions() {
        Options options = new Options();
        options.addOption("y", "yesterday", false, "fill in yesterday's data");
        options.addOption("d", "date", true, "fill in data for a given date");
        options.addOption("n", "dry-run", false, "don't fill in data");
        options.addOption("t", "debug", false, "don't say where in sheet data is going");
        return options;
    }

    private void setXlSheet() {
        InputStream inputStream = null;
        try { inputStream = new FileInputStream(excelFile); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        try { workbook = WorkbookFactory.create(inputStream); }
        catch (IOException | InvalidFormatException e) { e.printStackTrace(); }
        assert workbook != null;

        sheet = workbook.getSheet("MainSheet");
    }

    public void writeOut() {
        FileOutputStream fileOut = null;
        try { fileOut = new FileOutputStream(excelFile); }
        catch (FileNotFoundException e) { e.printStackTrace(); }

        try { workbook.write(fileOut); }
        catch (IOException e) { e.printStackTrace(); }

        try { if (fileOut != null)  fileOut.close(); }
        catch (IOException e) { e.printStackTrace(); }
    }
}
