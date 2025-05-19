package mg.sandratra.bakery.controllers.bmuser;

import lombok.RequiredArgsConstructor;
import mg.sandratra.bakery.controllers.BaseController;
import mg.sandratra.bakery.enums.Role;
import mg.sandratra.bakery.models.bmuser.BmUser;
import mg.sandratra.bakery.services.bmuser.BmUserService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class BmUserController extends BaseController {

    private final BmUserService bmUserService;

    // Display all users
    @GetMapping
    public ModelAndView getAllUsers() {
        List<BmUser> users = bmUserService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("users", users);
        return redirect(modelAndView, "pages/user/list", false); // Redirect to user list page
    }

    // Display form to create a new user
    @GetMapping("/form")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", new BmUser());
        modelAndView.addObject("roles", Role.values());
        return redirect(modelAndView, "pages/user/form", false); // Redirect to user creation form
    }

    // Display form to edit an existing user
    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        BmUser user = bmUserService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        modelAndView.addObject("roles", Role.values());
        return redirect(modelAndView, "pages/user/form", false); // Redirect to user edit form
    }

    // Handle the form submission to save or update a user
    @PostMapping("/save")
    public ModelAndView saveUser(@ModelAttribute BmUser user, RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();

        String page = "users";
        boolean isRedirect = true;
        try {
            bmUserService.saveOrUpdate(user); // Validation will be triggered here
            redirectAttributes.addFlashAttribute("message", "User saved successfully");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage()); // Handle validation error
            modelAndView.addObject("user", user); // Preserve submitted user data
            modelAndView.addObject("roles", Role.values());

            page = "pages/user/form";
            isRedirect = false;
        }
        return redirect(modelAndView, page, isRedirect); // Redirect to user list page after saving
    }

    // Delete a user
    @GetMapping("/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        bmUserService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully");
        return redirect(new ModelAndView(), "users", true); // Redirect to user list after deletion
    }
}
