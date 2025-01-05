package mg.sandratra.bakery.controllers.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class GlobalUserController {

    @ModelAttribute("loggedInUsername")
    public String getLoggedInUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername(); // Retrieve username
            } else {
                return principal.toString(); // Handle cases like OAuth where principal is a string
            }
        }
        return null; // Return null if no authentication
    }

    @ModelAttribute("loggedInRoles")
    public String getLoggedInRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().toString(); // Retrieve roles
        }
        return null; // Return null if no authentication
    }
}
