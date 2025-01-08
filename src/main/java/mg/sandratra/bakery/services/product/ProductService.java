package mg.sandratra.bakery.services.product;

import mg.sandratra.bakery.dto.product.ProductDto;
import mg.sandratra.bakery.dto.recipe.RecipeDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.repository.product.ProductRepository;
import mg.sandratra.bakery.services.recipe.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productDao;

    private final RecipeService recipeService;

    // Map Product to ProductDto
    public ProductDto mapToDto(Product product) {
        RecipeDto recipeDto = null;
        if (product.getRecipeId() != null) {
            // Fetch and map the recipe to RecipeDto using RecipeService
            recipeDto = recipeService.mapToDto(recipeService.findById(product.getRecipeId()));
        }

        return new ProductDto(
                product.getId(),
                recipeDto,
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt()
        );
    }

    // Map ProductDto to Product
    public Product mapToModel(ProductDto productDto) {
        Long recipeId = productDto.getRecipe() != null ? productDto.getRecipe().getId() : null;

        return new Product(
                productDto.getId(),
                recipeId,
                productDto.getName(),
                productDto.getPrice(),
                productDto.getStockQuantity(),
                productDto.getCreatedAt()
        );
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Product findById(Long id) {
        return productDao.findById(id);
    }

    public int saveOrUpdate(Product product) {
        validateProduct(product);
        if (product.getId() == null) {
            return productDao.save(product);
        } else {
            return productDao.update(product);
        }
    }

    public int deleteById(Long id) {
        return productDao.deleteById(id);
    }

    public void validateProduct(Product product) {
        Assert.hasText(product.getName(), "Product name must not be empty");
        Assert.notNull(product.getPrice(), "Product price must not be null");
        Assert.notNull(product.getRecipeId(), "Recipe cannot be null");
        Assert.isTrue(product.getPrice().compareTo(BigDecimal.ZERO) > 0, "Product price must be greater than zero");
        Assert.notNull(product.getStockQuantity(), "Stock quantity must not be null");
        Assert.isTrue(product.getStockQuantity() >= 0, "Stock quantity must not be negative");
    }
}
