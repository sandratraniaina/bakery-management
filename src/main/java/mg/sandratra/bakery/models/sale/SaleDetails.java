package mg.sandratra.bakery.models.sale;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetails {
    private Long id;
    private Long saleId;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private Timestamp createdAt;
}
