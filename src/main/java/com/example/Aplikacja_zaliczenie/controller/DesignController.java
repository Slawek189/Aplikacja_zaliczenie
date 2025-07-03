package com.example.Aplikacja_zaliczenie.controller;

import com.example.Aplikacja_zaliczenie.model.Property;
import com.example.Aplikacja_zaliczenie.model.User;
import com.example.Aplikacja_zaliczenie.repository.PropertyRepository;
import com.example.Aplikacja_zaliczenie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/design")
public class DesignController {

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("property", new Property());
        return "design";
    }

    @PostMapping
    public String processProperty(@ModelAttribute Property property,
                                  @RequestParam("photoFile") MultipartFile photoFile,
                                  Authentication auth) throws IOException {
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepo.findByUsername(username);
            property.setOwner(user);
        }
        if (photoFile != null && !photoFile.isEmpty()) {
            property.setPhoto(photoFile.getBytes());
        }
        propertyRepo.save(property);
        return "redirect:/properties";
    }
}

