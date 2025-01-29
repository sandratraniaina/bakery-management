package mg.sandratra.bakery.models.product.history;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceHistory {
    private Long id;
    private Long productId;
    private BigDecimal value;
    private Timestamp priceDate = Timestamp.valueOf(LocalDateTime.now());
}
