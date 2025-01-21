package mg.sandratra.bakery.controllers.sale;

import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.sale.SaleDto;
import mg.sandratra.bakery.services.sale.SaleService;
import mg.sandratra.bakery.services.sale.filter.SaleCustomerFilter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales/customers")

public class CustomerActivitiesController extends BaseController {

    private final SaleService saleService;

    @GetMapping
    public ModelAndView getAllSalesClient(@ModelAttribute SaleCustomerFilter customerFilter) {
        List<SaleDto> sales = saleService.search(customerFilter).stream().map(saleService::mapToDto).toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sales", sales);
        modelAndView.addObject("filter", customerFilter);
        return redirect(modelAndView, "pages/sale/client-list", false); // Redirect to sales list page
    }
}
