package mg.sandratra.bakery.services.bmuser.filter;

import java.sql.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@NoArgsConstructor
public class CommissionFilter implements Filter {
    private Long userId;
    
    private Date minDate;
    private Date maxDate;
    
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
        // Rest of your buildQuery implementation remains the same
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.id AS user_id, u.username, ")
                .append("SUM(s.total_amount) AS total_sale, ")
                .append("SUM(s.total_amount * 0.05) AS total_commission ")
                .append("FROM bm_user u ")
                .append("JOIN sale s ON u.id = s.created_by ");

        boolean hasCondition = false;

        if (userId != null) {
            query.append("WHERE u.id = ").append(userId).append(" ");
            hasCondition = true;
        }
        if (minDate != null) {
            query.append(hasCondition ? "AND " : "WHERE ")
                    .append("s.sale_date >= '").append(minDate).append("' ");
            hasCondition = true;
        }
        if (maxDate != null) {
            query.append(hasCondition ? "AND " : "WHERE ")
                    .append("s.sale_date <= '").append(maxDate).append("' ");
        }

        query.append("GROUP BY u.id, u.username");
        return query.toString();
    }
}