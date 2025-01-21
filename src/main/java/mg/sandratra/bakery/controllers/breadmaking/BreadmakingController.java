package mg.sandratra.bakery.controllers.breadmaking;

import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.breadmaking.BreadmakingDto;
import mg.sandratra.bakery.models.breadmaking.Breadmaking;
import mg.sandratra.bakery.services.breadmaking.BreadmakingService;
import mg.sandratra.bakery.services.breadmaking.filter.BreadkmakingFilter;
import mg.sandratra.bakery.services.ingredient.IngredientService;
import mg.sandratra.bakery.services.product.ProductService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/breadmaking")
public class BreadmakingController extends BaseController {

    private final BreadmakingService breadmakingService;
    private final ProductService productService;
    private final IngredientService ingredientService;

    // Display all breadmaking records
    @GetMapping
    public ModelAndView getAllBreadmakings(@ModelAttribute BreadkmakingFilter filter) {
        List<BreadmakingDto> breadmakings = breadmakingService.search(filter);
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("breadmakings", breadmakings);
        modelAndView.addObject("ingredients", ingredientService.findAll());
        modelAndView.addObject("filter", filter);
        return redirect(modelAndView, "pages/breadmaking/list", false); // Redirect to breadmaking list page
    }

    // Display form to create a new breadmaking record
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("breadmaking", new Breadmaking());
        modelAndView.addObject("products", productService.findAll());
        return redirect(modelAndView, "pages/breadmaking/form", false); // Redirect to create form
    }

    // Display form to edit an existing breadmaking record
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Breadmaking breadmaking = breadmakingService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("breadmaking", breadmaking);
        modelAndView.addObject("products", productService.findAll());
        return redirect(modelAndView, "pages/breadmaking/form", false); // Redirect to edit form
    }

    // Handle the form submission to save or update a breadmaking record
    @PostMapping("/save")
    public ModelAndView saveBreadmaking(@ModelAttribute Breadmaking breadmaking,
            RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        String page = "breadmaking";
        boolean isRedirect = true;
        try {
            breadmakingService.saveOrUpdate(breadmaking); // This will call validation
            redirectAttributes.addFlashAttribute("message", "Breadmaking record saved successfully");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView.addObject("error", e.getMessage()); // Catch validation error
            modelAndView.addObject("breadmaking", breadmaking); // Preserve the submitted breadmaking data
            modelAndView.addObject("products", productService.findAll());

            page = "pages/breadmaking/form";
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to breadmaking list page after saving
    }

    // Delete a breadmaking record
    @GetMapping("/delete/{id}")
    public ModelAndView deleteBreadmaking(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        breadmakingService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Breadmaking record deleted successfully");
        return redirect(new ModelAndView(), "breadmaking", true); // Redirect to breadmaking list after deletion
    }
}
