package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Section;
import com.crestama.crestamawebsite.entity.SectionImage;
import com.crestama.crestamawebsite.service.product.ProductService;
import com.crestama.crestamawebsite.service.section.SectionService;
import com.crestama.crestamawebsite.utility.S3Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;

import java.util.ArrayList;

@Controller
@RequestMapping("/sections")
public class SectionController {
    private ProductService productService;
    private SectionService sectionService;
    private S3Util s3Util;

    @Autowired
    public SectionController(ProductService productService, SectionService sectionService, S3Util s3Util) {
        this.productService = productService;
        this.sectionService = sectionService;
        this.s3Util = s3Util;
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
                       BindingResult result,
                       @PathVariable Long id, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            model.addAttribute("section", section);

            return "product/sectionForm";
        }

        section.setProduct(productService.findById(id));

        sectionService.save(section);

        return "redirect:/sections/product/" + id + "/sections";
    }

    @GetMapping("/product/{productId}/deleteSection/{sectionId}")
    public String deleteSection(@PathVariable Long productId, @PathVariable Long sectionId) {
        ArrayList<ObjectIdentifier> objects = new ArrayList<>();

        for (SectionImage image: sectionService.findById(sectionId).getImages()) {
            objects.add(ObjectIdentifier.builder()
                    .key("section-images/" + image.getId() + "/" + image.getImageName())
                    .build());
        }

        s3Util.deleteSectionImages(objects);

        sectionService.deleteById(sectionId);

        return "redirect:/sections/product/" + productId + "/sections";
    }
}
