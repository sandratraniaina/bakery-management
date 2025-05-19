package mg.sandratra.bakery.models.bmuser;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.sale.Sale;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commission {
    private BmUser employee;
    private BigDecimal totalCommission;
    private BigDecimal totalSale;
    private List<Sale> sales;

    public static final BigDecimal COMMISSION_THREESHOLD = BigDecimal.valueOf(200000);
}
