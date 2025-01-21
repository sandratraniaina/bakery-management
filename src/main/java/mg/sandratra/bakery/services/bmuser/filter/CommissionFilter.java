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
        return null;
    }
}
