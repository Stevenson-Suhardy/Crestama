package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Gallery;
import com.crestama.crestamawebsite.entity.SectionImage;
import com.crestama.crestamawebsite.service.section.SectionService;
import com.crestama.crestamawebsite.service.sectionImage.SectionImageService;
import com.crestama.crestamawebsite.utility.S3Util;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/sectionImages")
public class SectionImageController {
    private SectionService sectionService;
    private SectionImageService sectionImageService;

    @Autowired
    public SectionImageController(SectionService sectionService, SectionImageService sectionImageService) {
        this.sectionService = sectionService;
        this.sectionImageService = sectionImageService;
    }

    @GetMapping("/section/{id}/sectionImages")
    public String listOfSectionImages(@PathVariable Long id, Model model) {
        model.addAttribute("section", sectionService.findById(id));

        return "product/sectionDetails";
    }

    @GetMapping("/section/{id}/sectionImages/addImage")
    public String addImage(@PathVariable Long id, Model model) {
        model.addAttribute("sectionId", id);
        model.addAttribute("sectionImage", new SectionImage());

        return "product/sectionImageForm";
    }

    @PostMapping("/{id}/save")
    @Transactional
    public String save(@ModelAttribute @Valid SectionImage sectionImage,
                       BindingResult result,
                       @RequestParam("image") MultipartFile multipartFile,
                       @PathVariable Long id, Model model) {
        if (!ValidateImage(multipartFile)) {
            model.addAttribute("imageErr", "Section Image is required");
            model.addAttribute("sectionId", id);

            return "product/sectionImageForm";
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        sectionImage.setImagePath(fileName);
        sectionImage.setSection(sectionService.findById(id));
        SectionImage savedSectionImage = sectionImageService.save(sectionImage);

        String uploadDir = "section-images/" + savedSectionImage.getId() + "/";

        try {
            S3Util.uploadSectionImage(uploadDir + fileName, multipartFile.getInputStream());
        }
        catch (Exception e) {
            model.addAttribute("imageErr", "There was a problem with the image upload.");
            return "product/sectionImageForm";
        }

        return "redirect:/sectionImages/section/" + id + "/sectionImages";
    }

    @GetMapping("/section/{sectionId}/deleteImage/{sectionImageId}")
    @Transactional
    public String deleteItem(@PathVariable Long sectionId, @PathVariable Long sectionImageId) {
        try {
            SectionImage sectionImage = sectionImageService.findById(sectionImageId);

            S3Util.deleteObject("section-images/" + sectionImage.getId() + "/" + sectionImage.getImageName());
            sectionImageService.deleteById(sectionImageId);
        }
        catch (Exception e) {
            System.out.println("Cannot delete section image");
        }

        return "redirect:/sectionImages/section/" + sectionId + "/sectionImages";
    }

    private boolean ValidateImage(MultipartFile multipartFile) {
        if (multipartFile.getOriginalFilename() != null) {
            return !multipartFile.getOriginalFilename().isEmpty() && !multipartFile.getOriginalFilename().isBlank();
        }
        else {
            return false;
        }
    }
}
