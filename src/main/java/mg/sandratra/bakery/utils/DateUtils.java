package mg.sandratra.bakery.utils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtils {

    /**
     * Gets the month in text from an SQL Date.
     *
     * @param sqlDate the SQL Date object
     * @param locale  the Locale for the month name (e.g., Locale.ENGLISH, Locale.FRENCH)
     * @return the name of the month in the specified locale
     */
    public static String getMonthInText(Date sqlDate, Locale locale) {
        if (sqlDate == null || locale == null) {
            throw new IllegalArgumentException("sqlDate and locale must not be null");
        }

        // Convert to LocalDate
        LocalDate localDate = sqlDate.toLocalDate();

        // Get the month name in text   
        return localDate.getMonth().getDisplayName(TextStyle.FULL, locale);
    }
}
