package mg.sandratra.bakery.controllers.sale;

import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.dto.sale.SaleDto;
import mg.sandratra.bakery.services.sale.SaleService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")

public class ClientController extends BaseController {

    private final SaleService saleService;

    @GetMapping
    public ModelAndView getAllSalesClient(@RequestParam("name") Date date) {
        List<SaleDto> sales = saleService.findByDate(date).stream().map(saleService::mapToDto).toList();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sales", sales);
        return redirect(modelAndView, "pages/client/list", false); // Redirect to sales list page
    }
}
