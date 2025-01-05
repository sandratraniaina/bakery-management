package mg.sandratra.bakery.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import mg.sandratra.bakery.util.form.Option;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public ModelAndView redirectToIndex() {
        ModelAndView model = new ModelAndView();

        model.addObject("content", "index");

        model.addObject("options", List.of(
            new Option("1", "This is option 1"),
            new Option("2", "This is option 2"),
            new Option("3", "This is option 3")
        ));

        return redirect(model, "index", false);
    }

    @GetMapping("/login")
    public ModelAndView redirectToLogin(Model model) {
        return redirect(null, "pages/login", false);
    }
}