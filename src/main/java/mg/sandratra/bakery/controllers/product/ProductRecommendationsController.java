package mg.sandratra.bakery.controllers.product;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.product.ProductRecommendationsDto;
import mg.sandratra.bakery.dto.product.ProductRecommendationsFilter;
import mg.sandratra.bakery.models.product.ProductRecommendations;
import mg.sandratra.bakery.services.product.ProductRecommendationsService;
import mg.sandratra.bakery.services.product.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-recommendations")
public class ProductRecommendationsController extends BaseController {

    private final ProductRecommendationsService productRecommendationsService;
    private final ProductService productService;

    // Display all product recommendations
    @GetMapping
    public ModelAndView getAllRecommendations(@ModelAttribute ProductRecommendationsFilter filter) {
        List<ProductRecommendations> recommendations = productRecommendationsService.search(filter);

        List<ProductRecommendationsDto> recommendationDtos = recommendations.stream()
                .map(productRecommendationsService::mapToDto)
                .toList();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recommendations", recommendationDtos);
        modelAndView.addObject("filter", filter);
        return redirect(modelAndView, "pages/recommendation/list", false); // Redirect to recommendation list page
    }

    // Display form to create a new recommendation
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recommendation", new ProductRecommendations());
        modelAndView.addObject("products", productService.findAll());
        return redirect(modelAndView, "pages/recommendation/form", false); // Redirect to recommendation creation form
    }

    // Display form to edit an existing recommendation
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        ProductRecommendations recommendation = productRecommendationsService.findById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("recommendation", recommendation);
        return redirect(modelAndView, "pages/recommendation/form", false); // Redirect to recommendation edit form
    }

    // Handle form submission to save or update a recommendation
    @PostMapping("/save")
    public ModelAndView saveRecommendation(
            @ModelAttribute ProductRecommendations recommendation, 
            RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        String page = "product-recommendations";
        boolean isRedirect = true;
        try {
            productRecommendationsService.saveOrUpdate(recommendation);
            redirectAttributes.addFlashAttribute("message", "Recommendation saved successfully");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage()); // Handle validation error
            modelAndView.addObject("recommendation", recommendation); // Preserve submitted data
            modelAndView.addObject("products", productService.findAll());

            page = "pages/recommendation/form";
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to list page after saving
    }

    // Delete a recommendation
    @GetMapping("/delete/{id}")
    public ModelAndView deleteRecommendation(
            @PathVariable("id") Long id, 
            RedirectAttributes redirectAttributes) {
        productRecommendationsService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Recommendation deleted successfully");
        return redirect(new ModelAndView(), "product-recommendations", true); // Redirect to list after deletion
    }
}
