package mg.sandratra.bakery.dto.recipe;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.ingredient.Ingredient;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private BigDecimal quantity = new BigDecimal(0);
    private Ingredient ingredient;
}
