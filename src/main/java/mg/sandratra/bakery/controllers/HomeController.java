package mg.sandratra.bakery.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView redirectToIndex() {
        ModelAndView model = new ModelAndView();

        model.addObject("content", "index");

        return redirect(model, "index", false);
    }

    @GetMapping("/login")
    public ModelAndView redirectToLogin(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pages/login");

        return modelAndView;
    }
}