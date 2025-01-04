package mg.sandratra.bakery.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import mg.sandratra.bakery.util.form.Option;

@Controller
public class HomeController {

    @GetMapping("/")
    public String redirectToIndex(Model model) {
        model.addAttribute("content", "index");

        model.addAttribute("options", List.of(
            new Option("1", "This is option 1"),
            new Option("2", "This is option 2"),
            new Option("3", "This is option 3")
        ));

        return "layout";
    }

    @GetMapping("/login")
    public String redirectToLogin(Model model) {
        return "pages/login";
    }
}