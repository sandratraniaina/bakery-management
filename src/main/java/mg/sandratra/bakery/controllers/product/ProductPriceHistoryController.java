package mg.sandratra.bakery.controllers.product;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.product.ProductPriceHistoryDto;
import mg.sandratra.bakery.models.product.ProductPriceHistory;
import mg.sandratra.bakery.services.product.ProductPriceHistoryService;
import mg.sandratra.bakery.services.product.ProductService;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/products/price-history")
public class ProductPriceHistoryController extends BaseController {

    private final ProductPriceHistoryService productPriceHistoryService;
    private final ProductService productService;

    // Display all product price history with optional filtering
    @GetMapping
    public ModelAndView getAllPriceHistory(@ModelAttribute ProductPriceHistoryFilter filter) {
        List<ProductPriceHistory> priceHistories = productPriceHistoryService.filter(filter);
        
        List<ProductPriceHistoryDto> priceHistoryDtos = priceHistories.stream()
                .map(productPriceHistoryService::mapToDto)
                .toList();
        
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("histories", priceHistoryDtos);
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("products", productService.findAll()); // Populate products for filtering
        return redirect(modelAndView, "pages/product/price-history/list", false); // Redirect to price history list page
    }
}
