package mg.sandratra.bakery.services.recipe;

import mg.sandratra.bakery.dto.recipe.RecipeIngredientDto;
import mg.sandratra.bakery.models.ingredient.Ingredient;
import mg.sandratra.bakery.models.recipe.RecipeIngredient;
import mg.sandratra.bakery.repository.recipe.RecipeIngredientRepository;
import mg.sandratra.bakery.services.IngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientDao;

    private final IngredientService ingredientService;

    public List<RecipeIngredient> findAll() {
        return recipeIngredientDao.findAll();
    }

    public RecipeIngredient findById(Long recipeId, Long ingredientId) {
        return recipeIngredientDao.findById(recipeId, ingredientId);
    }

    public List<RecipeIngredient> findByRecipeId(Long recipeId) {
        return recipeIngredientDao.findByRecipeId(recipeId);
    }

    public int saveOrUpdate(RecipeIngredient recipeIngredient) {
        if (findById(recipeIngredient.getRecipeId(), recipeIngredient.getIngredientId()) == null) {
            return recipeIngredientDao.save(recipeIngredient);
        } else {
            return recipeIngredientDao.update(recipeIngredient);
        }
    }

    public int deleteById(Long recipeId, Long ingredientId) {
        return recipeIngredientDao.deleteById(recipeId, ingredientId);
    }

    public RecipeIngredientDto mapToDto(RecipeIngredient recipeIngredient) {
        if (recipeIngredient == null) {
            return null;
        }

        Ingredient ingredient = ingredientService.findById(recipeIngredient.getIngredientId());
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient not found for ID: " + recipeIngredient.getIngredientId());
        }

        return new RecipeIngredientDto(recipeIngredient.getQuantity(), ingredient);
    }

    public RecipeIngredient mapToModel(RecipeIngredientDto dto, Long recipeId) {
        if (dto == null || dto.getIngredient() == null) {
            return null;
        }

        Long ingredientId = dto.getIngredient().getId();
        if (ingredientId == null) {
            throw new IllegalArgumentException("Ingredient ID cannot be null.");
        }

        return new RecipeIngredient(recipeId, ingredientId, dto.getQuantity());
    }
}
