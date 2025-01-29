package mg.sandratra.bakery.services.product.history;

import mg.sandratra.bakery.dto.product.ProductPriceHistoryDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.product.history.ProductPriceHistory;
import mg.sandratra.bakery.repository.product.history.ProductPriceRepository;
import mg.sandratra.bakery.services.product.ProductService;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPriceHistoryService {

    private final ProductPriceRepository productPriceRepository;
    private final ProductService productService; // To fetch Product details

    // Map ProductPriceHistory to ProductPriceHistoryDto
    public ProductPriceHistoryDto mapToDto(ProductPriceHistory productPriceHistory) {
        Product product = productService.findById(productPriceHistory.getProductId());

        return new ProductPriceHistoryDto(
                productPriceHistory.getId(),
                product,
                productPriceHistory.getValue(),
                productPriceHistory.getPriceDate()
        );
    }

    // Map ProductPriceHistoryDto to ProductPriceHistory
    public ProductPriceHistory mapToModel(ProductPriceHistoryDto dto) {
        return new ProductPriceHistory(
                dto.getId(),
                dto.getProduct().getId(), // Extract productId from Product
                dto.getValue(),
                dto.getPriceDate()
        );
    }

    public List<ProductPriceHistory> findAll() {
        return productPriceRepository.findAll();
    }

    public ProductPriceHistory findById(Long id) {
        return productPriceRepository.findById(id);
    }

    public List<ProductPriceHistory> findByProductId(Long productId) {
        return productPriceRepository.findByProductId(productId);
    }

    public List<ProductPriceHistory> filter(ProductPriceHistoryFilter filter) {
        return productPriceRepository.filterProductPrices(filter);
    }

    public int saveOrUpdate(ProductPriceHistory productPriceHistory) {
        validateProductPriceHistory(productPriceHistory);
        if (productPriceHistory.getId() == null) {
            return productPriceRepository.save(productPriceHistory);
        } else {
            return productPriceRepository.update(productPriceHistory);
        }
    }

    public int deleteById(Long id) {
        return productPriceRepository.deleteById(id);
    }

    public void validateProductPriceHistory(ProductPriceHistory productPriceHistory) {
        Assert.notNull(productPriceHistory.getProductId(), "Product ID must not be null");
        Assert.notNull(productPriceHistory.getValue(), "Price value must not be null");
    }
}
