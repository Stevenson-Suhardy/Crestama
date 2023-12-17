package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private ProductService productService;
    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/manage")
    public String manage() {
        return "manage";
    }

    @GetMapping("/career")
    public String career() {
        return "career";
    }

    @GetMapping("/gallery")
    public String gallery(Model model) {
        model.addAttribute("products", productService.findAll());

        return "gallery";
    }
}
