package com.example.Aplikacja_zaliczenie.controller;

import com.example.Aplikacja_zaliczenie.model.User;
import com.example.Aplikacja_zaliczenie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Rejestracja – GET
    @GetMapping("/register")
    public String showRegistrationForm(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/"; // już zalogowany – przekieruj na stronę główną
        }
        model.addAttribute("user", new User());
        return "register";
    }

    // Rejestracja – POST
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("user") User user, Model model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Użytkownik o takim loginie już istnieje.");
            return "register";
        }
        userService.save(user);
        model.addAttribute("successMessage", "Rejestracja zakończona sukcesem. Możesz się teraz zalogować.");
        return "register";
    }

//    @GetMapping("/login")
//    public String showLoginForm() {
//        return "login";
//    }
//@GetMapping("/login")
//public String showLoginForm(Authentication authentication) {
//    if (authentication != null && authentication.isAuthenticated()
//            && !(authentication instanceof AnonymousAuthenticationToken)) {
//        return "redirect:/";  // użytkownik już zalogowany → od razu na stronę główną
//    }
//    return "login";  // pokazujemy login.html tylko niezalogowanym
//}
@GetMapping("/login")
public String showLoginForm(
        @RequestParam(value = "success", required = false) String success,
        Authentication authentication,
        Model model) {

    if (authentication != null && authentication.isAuthenticated()
            && !(authentication instanceof AnonymousAuthenticationToken)) {

        // Jeśli użytkownik jest zalogowany, ale NIE ma success=true → przekieruj bez komunikatu
        if (success == null) {
            return "redirect:/";
        }

        // Jeśli zalogowany i success=true → pokaż komunikat w login.html
        model.addAttribute("showSuccessMessage", true);
    }

    return "login";
}


    // Po poprawnym logowaniu Spring Security przekieruje tutaj
    @GetMapping("/login-success")
    public String loginSuccessRedirect() {
        return "redirect:/login?success=true";
    }
}
