package mg.sandratra.bakery.dto.product;

import java.time.Month;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommendationsDto {
    private Month month;
    private List<Product> products;
}
