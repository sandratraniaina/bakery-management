package mg.sandratra.bakery.dto.sale;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleDetailsDto {
    private Long id;
    private Product product;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    private Timestamp createdAt;

    public BigDecimal calculateSubTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
