package mg.sandratra.bakery.services.sale.filter;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.utils.filter.Filter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleCustomerFilter implements Filter {
    private Date saleDate;

    @Override
    public String buildQuery() {
        StringBuilder builder = new StringBuilder("SELECT * FROM sale WHERE 1 = 1");

        if (saleDate != null) {
            builder.append(" AND sale_date = ");
            builder.append(saleDate.toString());
        }

        return builder.toString();
    }
}
