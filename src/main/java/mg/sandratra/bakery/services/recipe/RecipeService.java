package mg.sandratra.bakery.services.recipe;

import mg.sandratra.bakery.dto.recipe.RecipeDto;
import mg.sandratra.bakery.dto.recipe.RecipeIngredientDto;
import mg.sandratra.bakery.models.recipe.Recipe;
import mg.sandratra.bakery.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeDao;

    private final RecipeIngredientService recipeIngredientService;

    // Map Recipe to RecipeDto
    public RecipeDto mapToDto(Recipe recipe) {
        // Get the list of RecipeIngredientDto from RecipeIngredientService
        List<RecipeIngredientDto> recipeIngredients = recipeIngredientService.findByRecipeId(recipe.getId()).stream()
                .map(recipeIngredientService::mapToDto) // Map each RecipeIngredient to RecipeIngredientDto
                .toList();

        // Map Recipe to RecipeDto
        return new RecipeDto(
                recipe.getId(),
                recipe.getName(),
                recipe.getDescription(),
                recipeIngredients,
                recipe.getCreatedAt());
    }

    // Map RecipeDto to Recipe
    public Recipe mapToModel(RecipeDto recipeDto) {
        return new Recipe(
                recipeDto.getId(),
                recipeDto.getName(),
                recipeDto.getDescription(),
                recipeDto.getCreatedAt());
    }

    public List<Recipe> findAll() {
        return recipeDao.findAll();
    }

    public Recipe findById(Long id) {
        return recipeDao.findById(id);
    }

    public int saveOrUpdate(Recipe recipe) {
        validateRecipe(recipe);
        if (recipe.getId() == null) {
            return recipeDao.save(recipe);
        } else {
            return recipeDao.update(recipe);
        }
    }

    public int deleteById(Long id) {
        return recipeDao.deleteById(id);
    }

    public void validateRecipe(Recipe recipe) {
        Assert.hasText(recipe.getName(), "Recipe name must not be empty");
        Assert.hasText(recipe.getDescription(), "Recipe description must not be empty");
        Assert.notNull(recipe.getCreatedAt(), "Recipe createdAt must not be null");
    }
}
