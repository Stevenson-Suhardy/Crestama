package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    @NotEmpty(message = "Product Name is required.")
    @NotNull(message = "Product Name is required.")
    private String name;

    @Column(name="image_path")
    private String imagePath;

    // Constructors

    public Product() {}

    public Product(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public String getImagePath() {
        if (imagePath == null || id == null) {
            return null;
        }

        return "/product-photos/" + id + "/" + imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Overridden methods

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
