package mg.sandratra.bakery.controllers;

import org.springframework.web.servlet.ModelAndView;

public class BaseController {
    protected ModelAndView redirect(ModelAndView modelAndView, String viewName, boolean isRedirect) {
        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        }

        if (isRedirect) {
            modelAndView.setViewName("redirect:/" + viewName);
        } else {
            modelAndView.setViewName(viewName);
        }

        return modelAndView;
    }
}
