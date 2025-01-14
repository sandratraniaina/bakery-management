package mg.sandratra.bakery.dto.product;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsDto {
    private Long id;
    private Date createdAt;
    private Product product;
}
