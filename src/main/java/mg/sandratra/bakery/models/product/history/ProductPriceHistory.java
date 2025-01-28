package mg.sandratra.bakery.models.product.history;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.product.Product;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceHistory {
    private Product product;
    private List<ProductPrice>  productPrices;
}
