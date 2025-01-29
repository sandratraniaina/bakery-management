package mg.sandratra.bakery.services.product.filter;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceHistoryFilter implements Filter {
    private Long productId;
    private Date maxDate;
    private Date minDate;

    // Remove @DateTimeFormat from the fields and only use it in the setter methods
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setMinDate(String minDate) {
        if (StringUtils.hasText(minDate)) {
            this.minDate = Date.valueOf(minDate);
        } else {
            this.minDate = null;
        }
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setMaxDate(String maxDate) {
        if (StringUtils.hasText(maxDate)) {
            this.maxDate = Date.valueOf(maxDate);
        } else {
            this.maxDate = null;
        }
    }

    // Add getters that return the string representation when needed
    public String getMinDate() {
        return minDate != null ? minDate.toString() : null;
    }

    public String getMaxDate() {
        return maxDate != null ? maxDate.toString() : null;
    }

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder("SELECT * FROM product_price_history WHERE 1=1");

        if (productId != null) {
            query.append(" AND product_id = :productId");
        }
        if (minDate != null) {
            query.append(" AND price_date >= :minDate");
        }
        if (maxDate != null) {
            query.append(" AND price_date <= :maxDate");
        }

        return query.toString();
    }

    public Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();

        if (productId != null) {
            parameters.put("productId", productId);
        }
        if (minDate != null) {
            parameters.put("minDate", minDate);
        }
        if (maxDate != null) {
            parameters.put("maxDate", maxDate);
        }

        return parameters;
    }
}