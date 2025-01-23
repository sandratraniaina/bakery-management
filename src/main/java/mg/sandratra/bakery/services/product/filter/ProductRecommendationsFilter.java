package mg.sandratra.bakery.services.product.filter;

import java.time.Month;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsFilter implements Filter {
    Month month;
    Integer year;

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM product_recommendations WHERE 1=1");
        if (year != null && year != 0) {
            query.append(" AND EXTRACT(YEAR FROM created_at)= ").append(year);
        }

        if (month != null) {
            query.append(" AND EXTRACT(MONTH FROM created_at)= ").append(month.getValue());
        }

        return query.toString();
    }
}
