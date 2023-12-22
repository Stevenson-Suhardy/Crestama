package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Gallery;
import com.crestama.crestamawebsite.entity.Product;
import com.crestama.crestamawebsite.service.gallery.GalleryService;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.service.section.SectionService;
import com.crestama.crestamawebsite.service.sectionImage.SectionImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {
    private GalleryService galleryService;
    private ProductService productService;

    @Autowired
    public HomeController(GalleryService galleryService, ProductService productService
    ) {
        this.galleryService = galleryService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.findAll());

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("products", productService.findAll());
        return "about";
    }

    @GetMapping("/manage")
    public String manage(Model model) {
        model.addAttribute("products", productService.findAll());

        return "manage";
    }

    @GetMapping("/career")
    public String career(Model model) {
        model.addAttribute("products", productService.findAll());

        return "career";
    }

    @GetMapping("/gallery-photos")
    public String gallery(Model model, @RequestParam("page") Optional<Integer> page,
                          @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(6);

        Page<Gallery> galleryPage = galleryService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("items", galleryPage);
        model.addAttribute("products", productService.findAll());

        int totalPages = galleryPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "gallery";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);

        model.addAttribute("product", product);
        model.addAttribute("products", productService.findAll());

        return "product";
    }
}
