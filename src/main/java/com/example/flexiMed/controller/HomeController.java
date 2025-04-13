package com.example.flexiMed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        // Adding a dynamic message to the template
        model.addAttribute("welcomeMessage", "Flexi Medical Dispatch");
        return "home"; // This will resolve to /templates/home.html
    }
}
