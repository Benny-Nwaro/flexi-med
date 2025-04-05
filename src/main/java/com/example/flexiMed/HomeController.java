package com.example.flexiMed;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Flexi Medical Dispatch ! <a href='flexi-med-front-itcp.vercel.app'>click here to begin</a>";
    }
}

