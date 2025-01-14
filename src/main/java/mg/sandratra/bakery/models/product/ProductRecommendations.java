package mg.sandratra.bakery.models.product;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendations {
    private Long id;
    private Long productId;
    private Date createdAt;
}
