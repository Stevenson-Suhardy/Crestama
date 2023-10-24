package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Product;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.utility.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/products")
public class ProductController {
    protected final String photoDirectory = "product-photos/";
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
    public String addProduct(Model model, HttpSession session) {
        model.addAttribute("product", new Product());

        return "product/productForm";
    }

    @GetMapping("/editProduct")
    public String editProduct(Model model, @RequestParam("productId") Long id) {
        Product product = productService.findById(id);

        model.addAttribute("product", product);

        return "product/productForm";
    }

    @Transactional
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

    @GetMapping("/deleteProduct")
    @Transactional
    public String deleteProduct(@RequestParam("productId") Long id) {
        try {
            deleteProductFolder(productService.findById(id));

            productService.deleteById(id);
        }
        catch (Exception e) {
            System.out.println("Cannot delete product images");
        }

        return "redirect:/products/products";
    }

    @Transactional
    public void deleteProductFolder(Product product) {
        try {
            File folder = new File(photoDirectory + product.getId());
            String[] files = folder.list();

            assert files != null;
            for (String file: files) {
                File currentFile = new File(folder.getPath(), file);
                if (currentFile.delete()) {
                    System.out.println("File deleted.");
                }
                else {
                    System.out.println("Cannot delete file.");
                }
            }
            if (folder.delete()) {
                System.out.println("Folder deleted.");
            }
            else {
                System.out.println("Cannot delete folder.");
            }
        }
        catch (Exception e) {
            System.out.println("Failed to delete image!");
        }
    }
}
