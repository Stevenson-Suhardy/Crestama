package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Product;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/products")
public class ProductController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads";
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

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product, @RequestParam("image") MultipartFile multipartFile)
            throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        product.setImagePath(fileName);
        Product savedProduct = productService.save(product);

        String uploadDir = "product-photos/" + savedProduct.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/products/products";
    }
}
