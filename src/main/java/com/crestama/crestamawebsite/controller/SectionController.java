package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Section;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.service.section.SectionService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sections")
public class SectionController {
    private ProductService productService;
    private SectionService sectionService;

    public SectionController(ProductService productService, SectionService sectionService) {
        this.productService = productService;
        this.sectionService = sectionService;
    }

    @GetMapping("/product/{id}/sections")
    public String listOfSections(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));

        return "product/productDetails";
    }

    @GetMapping("/product/{id}/sections/addSection")
    public String addSection(@PathVariable Long id, Model model) {
        model.addAttribute("productId", id);
        model.addAttribute("section", new Section());

        return "product/sectionForm";
    }

    @GetMapping("/product/{productId}/sections/editSection/{sectionId}")
    public String editSection(@PathVariable Long productId, @PathVariable Long sectionId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("section", sectionService.findById(sectionId));

        return "product/sectionForm";
    }

    @PostMapping("/{id}/save")
    public String save(@ModelAttribute @Valid Section section,
                       @PathVariable Long id,
                       BindingResult result) {
        if (result.hasErrors()) {
            return "product/sectionForm";
        }

        section.setProduct(productService.findById(id));

        sectionService.save(section);

        return "redirect:/sections/product/" + id + "/sections";
    }

    @GetMapping("/{productId}/deleteSection/{sectionId}")
    public String deleteSection(@PathVariable Long productId, @PathVariable Long sectionId) {
        sectionService.deleteById(sectionId);

        return "redirect:/sections/" + productId + "/sections";
    }
}
