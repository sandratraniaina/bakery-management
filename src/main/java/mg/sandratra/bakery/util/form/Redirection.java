package mg.sandratra.bakery.util.form;

import org.springframework.web.servlet.ModelAndView;

public class Redirection {
    private Redirection() {
    }

    public static ModelAndView redirect(ModelAndView modelAndView, String page) {
        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }

        modelAndView.addObject("content", page);

        return modelAndView;
    }
}
