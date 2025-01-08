package mg.sandratra.bakery.services.recipe;

import mg.sandratra.bakery.models.recipe.RecipeIngredient;
import mg.sandratra.bakery.repository.recipe.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientDao;

    public List<RecipeIngredient> findAll() {
        return recipeIngredientDao.findAll();
    }

    public RecipeIngredient findById(Long recipeId, Long ingredientId) {
        return recipeIngredientDao.findById(recipeId, ingredientId);
    }

    public RecipeIngredient findByRecipeId(Long recipeId) {
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
}
