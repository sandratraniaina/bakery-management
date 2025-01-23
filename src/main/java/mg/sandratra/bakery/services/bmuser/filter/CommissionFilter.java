package mg.sandratra.bakery.services.bmuser.filter;

import java.sql.Date;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@NoArgsConstructor
public class CommissionFilter implements Filter {
    private Long userId;
    
    private Date minDate;
    private Date maxDate;

    private Long genderId;
    
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
    StringBuilder query = new StringBuilder();
    query.append("SELECT u.user_id AS user_id, u.username, ")
            .append("SUM(s.total_amount) AS total_sale, ")
            .append("SUM(s.total_amount * 0.05) AS total_commission ")
            .append("FROM v_user_gender u ")
            .append("JOIN sale s ON u.user_id = s.created_by ");

    boolean hasCondition = false;

    // Add the condition for total_amount > 200000
    query.append("WHERE s.total_amount > " + Commission.COMMISSION_THREESHOLD + " ");
    hasCondition = true;

    if (userId != null) {
        query.append(hasCondition ? "AND " : "WHERE ")
                .append("u.user_id = ").append(userId).append(" ");
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
        hasCondition = true;
    }
    if (genderId != null) {
        query.append(hasCondition ? "AND " : "WHERE ")
                .append("u.gender_id = ").append(genderId).append(" ");
    }

    query.append("GROUP BY u.user_id, u.username");
    return query.toString();
}
}