package mg.sandratra.bakery.controllers.bmuser;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.models.bmuser.Commission;
import mg.sandratra.bakery.models.bmuser.CommissionSummary;
import mg.sandratra.bakery.services.bmuser.BmUserService;
import mg.sandratra.bakery.services.bmuser.CommissionService;
import mg.sandratra.bakery.services.bmuser.CommissionSummaryService;
import mg.sandratra.bakery.services.bmuser.GenderService;
import mg.sandratra.bakery.services.bmuser.filter.CommissionFilter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/commissions")
public class CommissionController extends BaseController {

    private final CommissionService commissionService;
    private final BmUserService bmUserService;
    private final GenderService genderService;
    private final CommissionSummaryService commissionSummaryService;

    @GetMapping
    public ModelAndView commissionsPage(@ModelAttribute CommissionFilter filter) {
        List<Commission> commissions = commissionService.search(filter);
        List<CommissionSummary> summaries = commissionSummaryService.getCommissionSummaries(filter.getGenderId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("filter", filter);
        modelAndView.addObject("users", bmUserService.findAll());
        modelAndView.addObject("commissions", commissions);
        modelAndView.addObject("genders", genderService.findAll()  );
        modelAndView.addObject("summaries", summaries);
        return redirect(modelAndView, "pages/user/commissions", false); // Redirect to user commissions page
    }
}
