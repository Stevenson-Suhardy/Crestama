package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Set;

@Entity(name="section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="images")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "section")
    private Collection<SectionImage> images;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public Section() {
    }

    public Section(String name, Collection<SectionImage> images, String description, Product product) {
        this.name = name;
        this.images = images;
        this.description = description;
        this.product = product;
    }

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

    public Collection<SectionImage> getImages() {
        return images;
    }

    public void setImages(Collection<SectionImage> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
