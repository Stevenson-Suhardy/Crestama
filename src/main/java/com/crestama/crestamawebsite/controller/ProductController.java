package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Product;
import com.crestama.crestamawebsite.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listOfProducts(Model model) {
        model.addAttribute("products", productService.findAll());

        return "product/listOfProducts";
    }

    @GetMapping("/addProduct")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());

        return "product/productForm";
    }

    @GetMapping("/editProduct/{id}")
    public String editProduct(Model model, @PathVariable Long id) {
        model.addAttribute("product", productService.findById(id));

        return "product/productForm";
    }

    @PostMapping("/save")
    @Transactional
    public String save(@ModelAttribute @Valid Product product,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "product/productForm";
        }

        productService.save(product);

        return "redirect:/products/products";
    }

    @GetMapping("/deleteProduct/{id}")
    @Transactional
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteById(id);

        return "redirect:/products/products";
    }
}
