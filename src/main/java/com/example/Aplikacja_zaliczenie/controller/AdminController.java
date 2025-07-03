package com.example.Aplikacja_zaliczenie.controller;

import com.example.Aplikacja_zaliczenie.model.User;
import com.example.Aplikacja_zaliczenie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String showUserPanel(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-panel";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null && !"ROLE_ADMIN".equals(user.getRole())) {
            userService.deleteById(id);
        }
        // Możesz dodać komunikat lub logowanie próby usunięcia admina
        return "redirect:/admin/users";
    }
}
