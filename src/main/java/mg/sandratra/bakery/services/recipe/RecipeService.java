package mg.sandratra.bakery.services.recipe;

import mg.sandratra.bakery.dto.recipe.RecipeDto;
import mg.sandratra.bakery.dto.recipe.RecipeIngredientDto;
import mg.sandratra.bakery.models.ingredient.Ingredient;
import mg.sandratra.bakery.models.recipe.Recipe;
import mg.sandratra.bakery.models.recipe.RecipeIngredient;
import mg.sandratra.bakery.repository.recipe.RecipeRepository;
import mg.sandratra.bakery.services.ingredient.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeIngredientService recipeIngredientService;
    private final IngredientService ingredientService;

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
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id);
    }

    @Transactional(rollbackFor = { Exception.class, SQLException.class })
    public Long saveRecipeDto(RecipeDto recipeDto) {
        Recipe recipe = mapToModel(recipeDto);
        Long result = recipe.getId();

        if (recipe.getId() != null) {
            recipeRepository.update(recipe);
        } else {
            result = recipeRepository.save(recipe);
        }

        if (result > 0) {
            for (RecipeIngredientDto recipeIngredientDto : recipeDto.getRecipeIngredients()) {
                if (recipeIngredientDto.getQuantity().compareTo(new BigDecimal(0)) <= 0) {
                    continue;
                }
                RecipeIngredient recipeIngredient = recipeIngredientService.mapToModel(recipeIngredientDto, result);
                recipeIngredientService.saveOrUpdate(recipeIngredient);
            }
        }

        return result;
    }

    public Long saveOrUpdate(Recipe recipe) {
        validateRecipe(recipe);
        if (recipe.getId() == null) {
            return recipeRepository.save(recipe);
        } else {
            return recipeRepository.update(recipe);
        }
    }

    public int deleteById(Long id) {
        return recipeRepository.deleteById(id);
    }

    public RecipeDto generateRecipeDto() {
        List<Ingredient> ingredients = ingredientService.findAll();
        
        List<RecipeIngredientDto> recipeIngredients = recipeIngredientService.generaIngredientDtosFromIngredients(ingredients); 

        return new RecipeDto(null, null, null, recipeIngredients, null);
    }

    public void validateRecipe(Recipe recipe) {
        Assert.hasText(recipe.getName(), "Recipe name must not be empty");
        Assert.hasText(recipe.getDescription(), "Recipe description must not be empty");
    }
}
