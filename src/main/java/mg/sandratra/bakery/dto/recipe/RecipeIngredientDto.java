package mg.sandratra.bakery.dto.recipe;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mg.sandratra.bakery.models.ingredient.Ingredient;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDto {
    private Long id;
    private int quantity;
    private List<Ingredient> ingredient;
}
