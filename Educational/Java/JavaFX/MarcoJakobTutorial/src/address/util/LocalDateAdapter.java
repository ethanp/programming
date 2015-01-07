package address.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

/**
 * Ethan Petuchowski 1/7/15
 *
 * adapter for JAXB to convert LocalDate object into 'yyyy-mm-dd' formatted string
 */
public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    /**
     * Convert a value type to a bound type.
     * @param v The value to be converted. Can be null.
     * @throws Exception if there's an error during the conversion.
     */
    @Override public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    /**
     * Convert a bound type to a value type.
     * @param v The value to be converted. Can be null.
     * @throws Exception if there's an error during the conversion.
     */
    @Override public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}
