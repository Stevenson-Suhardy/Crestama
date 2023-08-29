package com.crestama.crestamawebsite.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(HttpSession session) {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
