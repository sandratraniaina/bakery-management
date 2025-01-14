package mg.sandratra.bakery.dto.product;

import java.sql.Date;
import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsFilter implements Filter {
    Date date;

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM product_recommendations WHERE 1=1");

        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;

            query.append(" AND EXTRACT(YEAR FROM created_at) = ").append(year)
                    .append(" AND EXTRACT(MONTH FROM created_at) = ").append(month);

            query.append(" OR created_at < '").append(date).append("'");
        }

        return query.toString();
    }
}
