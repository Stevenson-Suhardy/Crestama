package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Product;
import com.crestama.crestamawebsite.entity.Section;
import com.crestama.crestamawebsite.entity.SectionImage;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.utility.S3Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.util.ArrayList;
import java.util.Collection;

@Controller
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;
    private S3Util s3Util;

    @Autowired
    public ProductController(ProductService productService, S3Util s3Util) {
        this.productService = productService;
        this.s3Util = s3Util;
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
        Collection<Section> sections = productService.findById(id).getSections();
        ArrayList<ObjectIdentifier> objects = new ArrayList<>();

        for (Section section: sections) {
            for (SectionImage image: section.getImages()) {
                objects.add(ObjectIdentifier.builder()
                        .key("section-images/" + image.getId() + "/" + image.getImageName())
                        .build());
            }
        }

        s3Util.deleteSectionImages(objects);

        productService.deleteById(id);

        return "redirect:/products/products";
    }
}
