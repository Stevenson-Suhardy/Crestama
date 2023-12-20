package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.Gallery;
import com.crestama.crestamawebsite.service.product.GalleryService;
import com.crestama.crestamawebsite.utility.FileUploadUtil;
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
import java.util.Objects;

@Controller
@RequestMapping("/gallery")
public class GalleryController {
    protected final String photoDirectory = "gallery-photos/";
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
        model.addAttribute("item", new Gallery());

        return "gallery/itemForm";
    }

    @GetMapping("/editItem/{id}")
    public String editItem(Model model, @PathVariable Long id) {
        Gallery gallery = galleryService.findById(id);

        model.addAttribute("item", gallery);

        return "gallery/itemForm";
    }

    @Transactional
    @PostMapping("/save")
    public String saveItem(@ModelAttribute @Valid Gallery gallery,
                              @RequestParam("image") MultipartFile multipartFile,
                              BindingResult result, Model model)
            throws IOException {
        if (result.hasErrors()) {
            if (multipartFile.getOriginalFilename() != null) {
                if (multipartFile.getOriginalFilename().isEmpty() || multipartFile.getOriginalFilename().isBlank()) {
                    model.addAttribute("imageErr", "Item Image is required");

                    return "gallery/itemForm";
                }
            }
            else {
                model.addAttribute("imageErr", "Item Image is required");

                return "gallery/itemForm";
            }
        }

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        gallery.setImagePath(fileName);
        Gallery savedGallery = galleryService.save(gallery);

        String uploadDir = "gallery-photos/" + savedGallery.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return "redirect:/gallery/items";
    }

    @GetMapping("/deleteItem/{id}")
    @Transactional
    public String deleteItem(@PathVariable Long id) {
        try {
            deleteGalleryFolder(galleryService.findById(id));

            galleryService.deleteById(id);
        }
        catch (Exception e) {
            System.out.println("Cannot delete gallery items");
        }

        return "redirect:/gallery/items";
    }

    @Transactional
    public void deleteGalleryFolder(Gallery gallery) {
        try {
            File folder = new File(photoDirectory + gallery.getId());
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
