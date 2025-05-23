package mg.sandratra.bakery.controllers.sale;

import mg.sandratra.bakery.config.UserDetails;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.sale.SaleDto;
import mg.sandratra.bakery.enums.ProductType;
import mg.sandratra.bakery.models.sale.Sale;
import mg.sandratra.bakery.services.sale.SaleDetailsService;
import mg.sandratra.bakery.services.sale.SaleService;
import mg.sandratra.bakery.services.sale.filter.SaleFilter;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales")
public class SaleController extends BaseController {

    private final SaleService saleService;
    private final SaleDetailsService saleDetailsService;

    private static final String FORM_PAGE = "pages/sale/form";
    private static final String MAIN_PAGE = "sales";

    // Display all sales
    @GetMapping
    public ModelAndView getAllSales(@ModelAttribute SaleFilter saleFilter) {
        List<SaleDto> sales = saleService.search(saleFilter).stream().map(saleService::mapToDto).toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(MAIN_PAGE, sales);
        modelAndView.addObject("productType", ProductType.values());
        modelAndView.addObject("saleFilter", saleFilter);
        modelAndView.addObject("booleanValues", SaleFilter.getBooleanValues());
        return redirect(modelAndView, "pages/sale/list", false); // Redirect to sales list page
    }

    

    // Display form to create a new sale
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sale", saleService.generateSaleDto());
        return redirect(modelAndView, FORM_PAGE, false); // Redirect to create form
    }

    // Display form to edit an existing sale
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        Sale sale = saleService.findById(id);
        SaleDto saleDto = saleService.mapToDto(sale);

        saleDto.getSaleDetails().addAll(saleDetailsService.getSaleDetailsNotAssignedProduct(id));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sale", saleDto);
        return redirect(modelAndView, FORM_PAGE, false); // Redirect to edit form
    }

    // Handle the form submission to save or update a sale
    @PostMapping("/save")
    public ModelAndView saveSale(@ModelAttribute SaleDto saleDto, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails user) {
        ModelAndView modelAndView = new ModelAndView();
        String page = MAIN_PAGE;
        boolean isRedirect = true;
        saleDto.setCreatedBy(user.getId());
        try {
            saleService.saveSale(saleDto);
            redirectAttributes.addFlashAttribute("message", "Sale saved successfully");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
            modelAndView.addObject("sale", saleDto);

            page = FORM_PAGE;
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to sales list page after saving
    }

    // Delete a sale
    @GetMapping("/delete/{id}")
    public ModelAndView deleteSale(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        saleService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Sale deleted successfully");
        return redirect(new ModelAndView(), MAIN_PAGE, true); // Redirect to sales list after deletion
    }
}
