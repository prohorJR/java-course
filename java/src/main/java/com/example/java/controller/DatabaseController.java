package com.example.java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DatabaseController {
    
    @GetMapping("/")
    public String home() {
        // Перенаправляем на H2 Console
        return "redirect:/h2-console";
    }
    
    @GetMapping("/h2")
    public String h2Direct() {
        return "redirect:/h2-console";
    }
}