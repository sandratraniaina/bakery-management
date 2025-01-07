package mg.sandratra.bakery.services;

import mg.sandratra.bakery.dao.IngredientDao;
import mg.sandratra.bakery.models.Ingredient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientDao ingredientDao;

    public List<Ingredient> findAll() {
        return ingredientDao.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientDao.findById(id);
    }

    public int saveOrUpdate(Ingredient ingredient) {
        if (ingredient.getId() == 0) {
            return ingredientDao.save(ingredient);
        } else {
            return ingredientDao.update(ingredient);
        }
    }

    public int deleteById(Long id) {
        return ingredientDao.deleteById(id);
    }
}
