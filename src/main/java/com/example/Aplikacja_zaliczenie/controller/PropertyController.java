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
import java.util.Base64;

@Controller
public class PropertyController {

    @Autowired
    private PropertyRepository propertyRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/properties")
    public String showProperties(Model model) {
        model.addAttribute("properties", propertyRepo.findAll());
        return "properties";
    }

    @GetMapping("/properties/{id}")
    public String propertyDetails(@PathVariable Long id, Model model, Authentication auth) {
        Property property = propertyRepo.findById(id).orElse(null);
        if (property == null) {
            return "redirect:/properties";
        }
        model.addAttribute("property", property);

        // Kodowanie zdjęcia do Base64 i dodanie do modelu
        if (property.getPhoto() != null && property.getPhoto().length > 0) {
            String photoBase64 = Base64.getEncoder().encodeToString(property.getPhoto());
            model.addAttribute("photoBase64", photoBase64);
        } else {
            model.addAttribute("photoBase64", null);
        }

        boolean canEdit = false;
        boolean isAdmin = false;

        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            User user = userRepo.findByUsername(username);
            isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin || (property.getOwner() != null && property.getOwner().getId().equals(user.getId()))) {
                canEdit = true;
            }
        }

        model.addAttribute("canEdit", canEdit);
        if (canEdit) {
            String editLink = isAdmin
                    ? "/admin/properties/edit/" + property.getId()
                    : "/user/properties/edit/" + property.getId();
            model.addAttribute("editLink", editLink);
        }

        return "property-details";
    }

    @PostMapping("/admin/properties/delete/{id}")
    public String deleteProperty(@PathVariable Long id, Authentication auth) {
        Property property = propertyRepo.findById(id).orElse(null);
        if (property == null) {
            return "redirect:/properties";
        }

        if (!canDeleteProperty(auth, property)) {
            return "error/403";
        }

        propertyRepo.deleteById(id);
        return "redirect:/properties";
    }

    private boolean canDeleteProperty(Authentication auth, Property property) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        String username = auth.getName();
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return false;
        }
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || (property.getOwner() != null && property.getOwner().getId().equals(user.getId()));
    }

    @GetMapping("/admin/properties/edit/{id}")
    public String editPropertyForm(@PathVariable Long id, Model model, Authentication auth) {
        Property property = propertyRepo.findById(id).orElse(null);
        if (property == null) {
            return "redirect:/properties";
        }

        if (!canEditProperty(auth, property)) {
            return "error/403";
        }

        model.addAttribute("property", property);

        // Dodanie zakodowanego zdjęcia do modelu
        if (property.getPhoto() != null && property.getPhoto().length > 0) {
            String photoBase64 = Base64.getEncoder().encodeToString(property.getPhoto());
            model.addAttribute("photoBase64", photoBase64);
        } else {
            model.addAttribute("photoBase64", null);
        }

        return "admin/edit-property";
    }

    @PostMapping("/admin/properties/edit/{id}")
    public String editPropertySave(@PathVariable Long id,
                                   @ModelAttribute Property property,
                                   @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                                   Authentication auth) throws IOException {
        Property existingProperty = propertyRepo.findById(id).orElse(null);
        if (existingProperty == null) {
            return "redirect:/properties";
        }

        if (!canEditProperty(auth, existingProperty)) {
            return "error/403";
        }

        existingProperty.setTitle(property.getTitle());
        existingProperty.setLocation(property.getLocation());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setType(property.getType());
        existingProperty.setPhoneNumber(property.getPhoneNumber());

        if (photoFile != null && !photoFile.isEmpty()) {
            existingProperty.setPhoto(photoFile.getBytes());
        }

        propertyRepo.save(existingProperty);
        return "redirect:/properties";
    }

    private boolean canEditProperty(Authentication auth, Property property) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        String username = auth.getName();
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return false;
        }
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || (property.getOwner() != null && property.getOwner().getId().equals(user.getId()));
    }

    @GetMapping("/user/properties/edit/{id}")
    public String editUserPropertyForm(@PathVariable Long id, Model model, Authentication auth) {
        Property property = propertyRepo.findById(id).orElse(null);
        if (property == null) return "redirect:/properties";

        if (!canEditProperty(auth, property)) return "error/403";

        model.addAttribute("property", property);

        // Dodanie zakodowanego zdjęcia do modelu dla usera
        if (property.getPhoto() != null && property.getPhoto().length > 0) {
            String photoBase64 = Base64.getEncoder().encodeToString(property.getPhoto());
            model.addAttribute("photoBase64", photoBase64);
        } else {
            model.addAttribute("photoBase64", null);
        }

        return "users/edit-property";
    }

    @PostMapping("/user/properties/edit/{id}")
    public String editUserPropertySave(@PathVariable Long id,
                                       @ModelAttribute Property property,
                                       @RequestParam(value = "photoFile", required = false) MultipartFile photoFile,
                                       Authentication auth) throws IOException {
        Property existingProperty = propertyRepo.findById(id).orElse(null);
        if (existingProperty == null) return "redirect:/properties";

        if (!canEditProperty(auth, existingProperty)) return "error/403";

        existingProperty.setTitle(property.getTitle());
        existingProperty.setLocation(property.getLocation());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setType(property.getType());
        existingProperty.setPhoneNumber(property.getPhoneNumber());

        if (photoFile != null && !photoFile.isEmpty()) {
            existingProperty.setPhoto(photoFile.getBytes());
        }

        propertyRepo.save(existingProperty);
        return "redirect:/properties";
    }
}
