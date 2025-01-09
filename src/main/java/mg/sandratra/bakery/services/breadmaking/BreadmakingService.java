package mg.sandratra.bakery.services.breadmaking;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.dto.breadmaking.BreadmakingDto;
import mg.sandratra.bakery.dto.product.ProductDto;
import mg.sandratra.bakery.models.breadmaking.Breadmaking;
import mg.sandratra.bakery.repository.breadmaking.BreadmakingRepository;
import mg.sandratra.bakery.services.product.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BreadmakingService {

    private final BreadmakingRepository breadmakingDao;
    private final ProductService productService;

    // Map Breadmaking to BreadmakingDto
    public BreadmakingDto mapToDto(Breadmaking breadmaking) {
        // Use ProductService to find product and map it to ProductDto
        ProductDto productDto = productService.mapToDto(productService.findById(breadmaking.getProductId()));

        // Map Breadmaking to BreadmakingDto
        return new BreadmakingDto(
                breadmaking.getId(),
                productDto,
                breadmaking.getCreatedBy(),
                breadmaking.getQuantity(),
                breadmaking.getProductionDate(),
                breadmaking.getIngredientCost(),
                breadmaking.getOtherCost(),
                breadmaking.getCostPerUnit()
        );
    }

    // Map BreadmakingDto to Breadmaking
    public Breadmaking mapToModel(BreadmakingDto breadmakingDto) {
        return new Breadmaking(
                breadmakingDto.getId(),
                breadmakingDto.getProduct().getId(),
                breadmakingDto.getCreatedBy(),
                breadmakingDto.getQuantity(),
                breadmakingDto.getProductionDate(),
                breadmakingDto.getIngredientCost(),
                breadmakingDto.getOtherCost(),
                breadmakingDto.getCostPerUnit()
        );
    }

    public List<Breadmaking> findAll() {
        return breadmakingDao.findAll();
    }

    public Breadmaking findById(Long id) {
        return breadmakingDao.findById(id);
    }

    public List<BreadmakingDto> findByIngredientId(Long ingredientId) {
        if (ingredientId != null) {
            return List.of(mapToDto(breadmakingDao.findByIngredientId(ingredientId)));
        } else {
            return findAll().stream()
                    .map(this::mapToDto)
                    .toList();
        }
    }

    public int saveOrUpdate(Breadmaking breadmaking) {
        validateBreadmaking(breadmaking);
        if (breadmaking.getId() == null) {
            return breadmakingDao.save(breadmaking);
        } else {
            return breadmakingDao.update(breadmaking);
        }
    }

    public int deleteById(Long id) {
        return breadmakingDao.deleteById(id);
    }

    private void validateBreadmaking(Breadmaking breadmaking) {
        Assert.notNull(breadmaking.getProductId(), "Product ID must not be null");
        Assert.notNull(breadmaking.getProductionDate(), "Production date must not be null");
        Assert.notNull(breadmaking.getQuantity(), "Quantity must not be null");
        Assert.notNull(breadmaking.getIngredientCost(), "Ingredient cost must not be null");
    }
}
