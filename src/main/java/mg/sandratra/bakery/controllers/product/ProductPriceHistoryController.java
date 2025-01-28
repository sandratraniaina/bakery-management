package mg.sandratra.bakery.controllers.product;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.models.product.history.ProductPriceHistory;
import mg.sandratra.bakery.services.product.history.ProductPriceHistoryService;
import mg.sandratra.bakery.services.product.filter.ProductPriceHistoryFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-price-history")
public class ProductPriceHistoryController extends BaseController{

    private final ProductPriceHistoryService productPriceHistoryService;

    /**
     * Display the product price history based on the filter criteria.
     *
     * @param filter The filter object containing criteria for the history.
     * @return A ModelAndView displaying the filtered product price history.
     */
    @GetMapping
    public ModelAndView filterProductPriceHistory(@ModelAttribute ProductPriceHistoryFilter filter) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            ProductPriceHistory history = productPriceHistoryService.fitlerHistory(filter);
            modelAndView.addObject("history", history);
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }

        modelAndView.addObject("filter", filter);
        return redirect(modelAndView, "pages/product/price-history", false);
    }
}
