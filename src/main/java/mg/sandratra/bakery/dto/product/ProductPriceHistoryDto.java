package mg.sandratra.bakery.dto.product;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceHistoryDto {
    private Long id;
    private Product product;
    private BigDecimal value;
    private Timestamp priceDate = Timestamp.valueOf(LocalDateTime.now());
}
