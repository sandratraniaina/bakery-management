package mg.sandratra.bakery.controllers.ingredient;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.enums.Unit;
import mg.sandratra.bakery.models.ingredient.Ingredient;
import mg.sandratra.bakery.services.IngredientService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ingredients")
public class IngredientController extends BaseController {
    private final IngredientService ingredientService;

    // Display all ingredients
    @GetMapping
    public ModelAndView getAllIngredients() {
        List<Ingredient> ingredients = ingredientService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ingredients", ingredients);
        return redirect(modelAndView, "pages/ingredient/list", false); // Redirect to ingredient list page
    }

    // Display form to create a new ingredient
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ingredient", new Ingredient());
        modelAndView.addObject("units", Unit.values());
        return redirect(modelAndView, "pages/ingredient/form", false); // Redirect to ingredient form page
    }

    // Handle the form submission to save or update an ingredient
    @PostMapping("/save")
    public ModelAndView saveIngredient(@ModelAttribute Ingredient ingredient) {
        ingredientService.saveOrUpdate(ingredient);
        return redirect(new ModelAndView(), "ingredients", true); // Redirect to ingredients list after saving
    }

    // Display form to edit an existing ingredient
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Ingredient ingredient = ingredientService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("ingredient", ingredient);
        modelAndView.addObject("units", Unit.values());
        return redirect(modelAndView, "pages/ingredient/form", false); // Redirect to ingredient form page
    }

    // Delete an ingredient
    @GetMapping("/delete/{id}")
    public ModelAndView deleteIngredient(@PathVariable("id") Long id) {
        ingredientService.deleteById(id);
        return redirect(new ModelAndView(), "ingredients", true); // Redirect to ingredients list after deletion
    }
}
