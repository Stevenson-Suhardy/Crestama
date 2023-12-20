package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Gallery;
import com.crestama.crestamawebsite.service.gallery.GalleryService;
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

import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
    private GalleryService galleryService;

    @Autowired
    public GalleryController(GalleryService productService) {
        this.galleryService = productService;
    }

    @GetMapping("/items")
    public String listOfItems(Model model) {
        model.addAttribute("items", galleryService.findAll());

        return "gallery/listOfItems";
    }

    @GetMapping("/addItem")
    public String addItem(Model model) {
        model.addAttribute("gallery", new Gallery());

        return "gallery/itemForm";
    }

    @GetMapping("/editItem/{id}")
    public String editItem(Model model, @PathVariable Long id) {
        Gallery gallery = galleryService.findById(id);

        model.addAttribute("gallery", gallery);

        return "gallery/itemForm";
    }

    @Transactional
    @PostMapping("/save")
    public String saveItem(@ModelAttribute @Valid Gallery gallery,
                           BindingResult result,
                           @RequestParam("image") MultipartFile multipartFile,
                           Model model)
            throws IOException {
        if (result.hasErrors()) {
            if (multipartFile.getOriginalFilename() != null) {
                if (multipartFile.getOriginalFilename().isEmpty() || multipartFile.getOriginalFilename().isBlank()) {
                    model.addAttribute("imageErr", "Item Image is required");

                    return "gallery/itemForm";
                }
                else {
                    return "gallery/itemForm";
                }
            }
            else {
                model.addAttribute("imageErr", "Item Image is required");
            }
            return "gallery/itemForm";
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        gallery.setImagePath(fileName);
        Gallery savedGallery = galleryService.save(gallery);

        String uploadDir = "gallery-photos/" + savedGallery.getId() + "/";

        try {
            S3Util.uploadImage(uploadDir + fileName, multipartFile.getInputStream());
        }
        catch (Exception e) {
            model.addAttribute("imageErr", "There was a problem with the image upload.");
            return "gallery/itemForm";
        }

        return "redirect:/gallery/items";
    }

    @GetMapping("/deleteItem/{id}")
    @Transactional
    public String deleteItem(@PathVariable Long id) {
        try {
            Gallery item = galleryService.findById(id);

            S3Util.deleteObject("gallery-photos/" + item.getId() + "/" + item.getImageName());
            galleryService.deleteById(id);
        }
        catch (Exception e) {
            System.out.println("Cannot delete gallery items");
        }

        return "redirect:/gallery/items";
    }
}
