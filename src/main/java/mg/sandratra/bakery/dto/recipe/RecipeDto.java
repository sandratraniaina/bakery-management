package mg.sandratra.bakery.dto.recipe;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long it;
    private String name;
    private String description;
    private RecipeIngredientDto recipeIngredient;
    private Timestamp createdAt;
}
