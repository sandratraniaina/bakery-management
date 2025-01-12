package mg.sandratra.bakery.controllers.recipe;

import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.recipe.RecipeDto;
import mg.sandratra.bakery.models.recipe.Recipe;
import mg.sandratra.bakery.services.recipe.RecipeService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recipes")
public class RecipeController extends BaseController{

    private final RecipeService recipeService;

    // Display all recipes
    @GetMapping
    public ModelAndView getAllRecipes() {
        List<RecipeDto> recipes = recipeService.findAll().stream().map(recipeService::mapToDto).toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recipes", recipes);
        return redirect(modelAndView, "pages/recipe/list", false); // Redirect to recipe list page
    }

    // Display form to create a new recipe
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recipe", recipeService.generateRecipeDto());
        return redirect(modelAndView, "pages/recipe/form", false); // Redirect to create form
    }

    // Display form to edit an existing recipe
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Recipe recipe = recipeService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recipe", recipe);
        return redirect(modelAndView, "pages/recipe/form", false); // Redirect to edit form
    }

    // Handle the form submission to save or update a recipe
    @PostMapping("/save")
    public ModelAndView saveRecipe(@ModelAttribute("recipe") RecipeDto recipeDto, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        String page = "recipes";
        boolean isRedirect = true;
        try {
            recipeService.saveRecipeDto(recipeDto); // This will call validation
            redirectAttributes.addFlashAttribute("message", "Recipe saved successfully");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage()); // Catch validation error
            modelAndView.addObject("recipe", recipeDto); // Catch validation error

            page = "pages/recipe/form";
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to recipes list page after saving
    }

    // Delete a recipe
    @GetMapping("/delete/{id}")
    public ModelAndView deleteRecipe(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        recipeService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Recipe deleted successfully");
        return redirect(new ModelAndView(), "recipes", true); // Redirect to recipes list after deletion
    }
}
