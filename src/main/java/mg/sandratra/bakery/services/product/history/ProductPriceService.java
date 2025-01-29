package mg.sandratra.bakery.services.product.history;

import mg.sandratra.bakery.models.product.history.ProductPriceHistory;
import mg.sandratra.bakery.repository.product.history.ProductPriceRepository;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    public List<ProductPriceHistory> findAll() {
        return productPriceRepository.findAll();
    }

    public ProductPriceHistory findById(Long id) {
        return productPriceRepository.findById(id);
    }

    public List<ProductPriceHistory> findByProductId(Long productId) {
        return productPriceRepository.findByProductId(productId);
    }

    public List<ProductPriceHistory> filterProductPrices(ProductPriceHistoryFilter filter) {
        return productPriceRepository.filterProductPrices(filter);
    }

    public int saveOrUpdate(ProductPriceHistory productPrice) {
        validateProductPrice(productPrice);
        if (productPrice.getId() == null) {
            return productPriceRepository.save(productPrice);
        } else {
            return productPriceRepository.update(productPrice);
        }
    }

    public int deleteById(Long id) {
        return productPriceRepository.deleteById(id);
    }

    public void validateProductPrice(ProductPriceHistory productPrice) {
        Assert.notNull(productPrice.getProduct(), "Product must not be null");
        Assert.notNull(productPrice.getValue(), "Price value must not be null");
        Assert.notNull(productPrice.getPriceDate(), "Price date must not be null");
    }
}