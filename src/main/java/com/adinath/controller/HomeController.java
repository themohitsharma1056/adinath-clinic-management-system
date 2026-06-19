package com.adinath.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/doctors")
    public String doctors() {
        return "doctors";
    }

    @GetMapping("/appointment")
    public String appointment() {
        return "appointment";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}