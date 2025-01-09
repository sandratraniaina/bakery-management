package mg.sandratra.bakery.models.recipe;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    private Long recipeId;
    private Long ingredientId;
    private BigDecimal quantity;
}
