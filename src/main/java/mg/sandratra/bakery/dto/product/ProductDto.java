package mg.sandratra.bakery.dto.product;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.dto.recipe.RecipeDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private RecipeDto recipe;
    private String name;
    private BigDecimal price;
    private Long stockQuantity;
    private Timestamp createdAt;
}
