package mg.sandratra.bakery.services.ingredient;

import mg.sandratra.bakery.models.ingredient.Ingredient;
import mg.sandratra.bakery.repository.ingredient.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientDao;

    public List<Ingredient> findAll() {
        return ingredientDao.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientDao.findById(id);
    }

    public List<Ingredient> getNotRecipeAssigned(Long recipeId) { 
        return ingredientDao.getNotRecipeAssigned(recipeId);
    }

    public int saveOrUpdate(Ingredient ingredient) {
        if (ingredient.getId() == null) {
            return ingredientDao.save(ingredient);
        } else {
            return ingredientDao.update(ingredient);
        }
    }

    public int deleteById(Long id) {
        return ingredientDao.deleteById(id);
    }
}
