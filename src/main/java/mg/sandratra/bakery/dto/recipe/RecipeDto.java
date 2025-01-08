package mg.sandratra.bakery.dto.recipe;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;
    private String name;
    private String description;
    private List<RecipeIngredientDto> recipeIngredients;
    private Timestamp createdAt;
}
