package address.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Ethan Petuchowski 1/6/15
 *
 * helper functions for dates
 */
public class DateUtil {

    private static final String DATE_PATTERN = "MM/dd/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

    /** convert LocalDate to String */
    public static String format(LocalDate date) {
        if (date == null) return null;
        return DATE_FORMATTER.format(date);
    }

    /** convert String to LocalDate */
    public static LocalDate parse(String dateString) {
        try {
            return DATE_FORMATTER.parse(dateString, LocalDate::from);
        }
        catch (DateTimeParseException e) {
            return null;
        }
    }

    public static boolean validDate(String dateString) {
        return DateUtil.parse(dateString) != null;
    }
}
