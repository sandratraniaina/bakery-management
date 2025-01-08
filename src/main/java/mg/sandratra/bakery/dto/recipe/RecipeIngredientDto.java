package mg.sandratra.bakery.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.ingredient.Ingredient;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private int quantity;
    private Ingredient ingredient;
}
