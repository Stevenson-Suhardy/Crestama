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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "sectionImage")
    private Collection<SectionImage> images;

    @Column(name="description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
}
