package mg.sandratra.bakery.controllers.product;

import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.product.ProductDto;
import mg.sandratra.bakery.models.product.Product;
import mg.sandratra.bakery.services.product.ProductService;
import mg.sandratra.bakery.services.recipe.RecipeService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final RecipeService recipeService;

    // Display all products
    @GetMapping
    public ModelAndView getAllProducts() {
        List<ProductDto> products = productService.findAll().stream().map(productService::mapToDto).toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("products", products);
        return redirect(modelAndView, "pages/product/list", false); // Redirect to product list page
    }

    // Display form to create a new product
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product", new Product());
        modelAndView.addObject("recipes", recipeService.findAll());
        return redirect(modelAndView, "pages/product/form", false); // Redirect to create form
    }

    // Display form to edit an existing product
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Product product = productService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("product", product);
        modelAndView.addObject("recipes", recipeService.findAll());
        return redirect(modelAndView, "pages/product/form", false); // Redirect to edit form
    }

    // Handle the form submission to save or update a product
    @PostMapping("/save")
    public ModelAndView saveProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        String page = "products";
        boolean isRedirect = true;
        try {
            productService.saveOrUpdate(product); // This will call validation
            redirectAttributes.addFlashAttribute("message", "Product saved successfully");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage()); // Catch validation error
            modelAndView.addObject("product", product); // Preserve the submitted product data
            modelAndView.addObject("recipes", recipeService.findAll());

            page = "pages/product/form";
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to products list page after saving
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        productService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully");
        return redirect(new ModelAndView(), "products", true); // Redirect to products list after deletion
    }
}
