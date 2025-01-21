package mg.sandratra.bakery.services.bmuser.filter;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommissionFilter implements Filter{
    private Long userId;
    private Date minDate;
    private Date maxDate;

    @Override
    public String buildQuery() {
        StringBuilder query = new StringBuilder();
        query.append("SELECT u.id AS user_id, u.username, ")
             .append("SUM(s.total_amount) AS total_sale, ")
             .append("SUM(s.total_amount * 0.5) AS total_commission ")
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
