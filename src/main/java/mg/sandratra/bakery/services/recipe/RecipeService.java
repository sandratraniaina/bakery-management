package mg.sandratra.bakery.services.recipe;

import mg.sandratra.bakery.models.recipe.Recipe;
import mg.sandratra.bakery.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeDao;

    public List<Recipe> findAll() {
        return recipeDao.findAll();
    }

    public Recipe findById(Long id) {
        return recipeDao.findById(id);
    }

    public int saveOrUpdate(Recipe recipe) {
        if (recipe.getId() == null) {
            return recipeDao.save(recipe);
        } else {
            return recipeDao.update(recipe);
        }
    }

    public int deleteById(Long id) {
        return recipeDao.deleteById(id);
    }
}
