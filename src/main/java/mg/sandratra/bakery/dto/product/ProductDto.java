package mg.sandratra.bakery.dto.product;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.dto.recipe.RecipeDto;
import mg.sandratra.bakery.enums.ProductType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private RecipeDto recipe;
    private String name;
    private ProductType productType;
    private BigDecimal price;
    private Long stockQuantity;
    private Timestamp createdAt;
}
