package mg.sandratra.bakery.utils.date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import java.time.Month;

@Component
public class MonthConverter implements Converter<String, Month> {

    @Override
    public Month convert(String source) {
        try {
            int monthValue = Integer.parseInt(source); // Convert the string to an integer
            return Month.of(monthValue); // Convert the integer to the corresponding Month enum
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid month value: " + source, e);
        }
    }
}
