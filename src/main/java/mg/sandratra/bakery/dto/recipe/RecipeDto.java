package mg.sandratra.bakery.dto.recipe;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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

    public String getIngredientNames() {
        // Check if the list is empty or null
        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            return "";
        }

        // Use stream to map the RecipeIngredientDto to the ingredient name and join
        // them with commas
        return recipeIngredients.stream()
                .map(recipeIngredientDto -> recipeIngredientDto.getIngredient().getName()) // Get ingredient name
                .collect(Collectors.joining(", ")); // Join names with commas
    }
}
