package mg.sandratra.bakery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToIndex(Model model) {
        model.addAttribute("content", "index");
        return "layout";
    }

    @GetMapping("/login")
    public String redirectToLogin(Model model) {
        return "pages/login";
    }
}
