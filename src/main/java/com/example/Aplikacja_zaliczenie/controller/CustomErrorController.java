package com.example.Aplikacja_zaliczenie.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        String message;
        switch (statusCode) {
            case 404 -> message = "Strona nie została znaleziona.";
            case 403 -> message = "Brak dostępu do tej strony.";
            case 500 -> message = "Wystąpił błąd serwera.";
            default -> message = "Wystąpił nieznany błąd.";
        }

        model.addAttribute("errorCode", statusCode);
        model.addAttribute("errorMessage", message);
        return "error/custom";
    }

    // (Opcjonalnie) usuń ten override jeśli niepotrzebny:
    public String getErrorPath() {
        return "/error";
    }
}
