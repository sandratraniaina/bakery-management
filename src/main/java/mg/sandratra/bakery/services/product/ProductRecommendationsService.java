package mg.sandratra.bakery.services.product;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.dto.product.ProductRecommendationsDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.models.product.ProductRecommendations;
import mg.sandratra.bakery.repository.product.ProductRecommendationsRepository;
import mg.sandratra.bakery.repository.product.ProductRepository;
import mg.sandratra.bakery.services.product.filter.ProductRecommendationsFilter;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.logging.Filter;

@Service
@RequiredArgsConstructor
public class ProductRecommendationsService {

    private final ProductRecommendationsRepository productRecommendationsRepository;
    private final ProductRepository productRepository;

    // Map ProductRecommendations to ProductRecommendationsDto
    public ProductRecommendationsDto mapToDto(ProductRecommendations recommendation) {
        // Fetch the product associated with the recommendation
        Product product = productRepository.findById(recommendation.getProductId());

        // Map ProductRecommendations to ProductRecommendationsDto
        return new ProductRecommendationsDto(
                recommendation.getId(),
                recommendation.getCreatedAt(),
                product
        );
    }

    // Map ProductRecommendationsDto to ProductRecommendations
    public ProductRecommendations mapToModel(ProductRecommendationsDto recommendationDto) {
        return new ProductRecommendations(
                recommendationDto.getId(),
                recommendationDto.getProduct().getId(),
                recommendationDto.getCreatedAt()
        );
    }

    public List<ProductRecommendations> search(ProductRecommendationsFilter filter) {
        if (filter.getDate() == null) {
            return productRecommendationsRepository.findAll();
        } else {
            return productRecommendationsRepository.search(filter);
        }
    }

    public List<ProductRecommendations> findAll() {
        return productRecommendationsRepository.findAll();
    }

    public ProductRecommendations findById(Long id) {
        return productRecommendationsRepository.findById(id);
    }

    public int saveOrUpdate(ProductRecommendations recommendation) {
        validateRecommendation(recommendation);
        if (recommendation.getId() == null) {
            return productRecommendationsRepository.save(recommendation);
        } else {
            return productRecommendationsRepository.update(recommendation);
        }
    }

    public int deleteById(Long id) {
        return productRecommendationsRepository.deleteById(id);
    }

    public void validateRecommendation(ProductRecommendations recommendation) {
        Assert.notNull(recommendation.getProductId(), "Product ID must not be null");
        Assert.notNull(recommendation.getCreatedAt(), "Creation date must not be null");
    }
}
