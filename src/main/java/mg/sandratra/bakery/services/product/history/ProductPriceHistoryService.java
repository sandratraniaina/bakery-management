package mg.sandratra.bakery.services.product.history;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.product.history.ProductPrice;
import mg.sandratra.bakery.models.product.history.ProductPriceHistory;
import mg.sandratra.bakery.services.product.ProductService;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;

@Service
@RequiredArgsConstructor
public class ProductPriceHistoryService {

    private final ProductService productService;
    private final ProductPriceService productPriceService;

    public ProductPriceHistory mapToProductPriceHistory(List<ProductPrice> productPrices) {
        Long productId = Long.valueOf(0);
        Product product = new Product();
        product.setId(Long.valueOf(1));
        if (!productPrices.isEmpty()) {
            productId = productPrices.get(0).getProduct().getId();
            product = productService.findById(productId);
        }

        // Set the Product object in each ProductPrice
        List<ProductPrice> updatedProductPrices = productPrices;

        // Create and return the ProductPriceHistory object
        return new ProductPriceHistory(product, updatedProductPrices);
    }

    public ProductPriceHistory fitlerHistory(ProductPriceHistoryFilter filter) {
        List<ProductPrice> productPrices = productPriceService.filterProductPrices(filter);
        return mapToProductPriceHistory(productPrices);
    }
}
