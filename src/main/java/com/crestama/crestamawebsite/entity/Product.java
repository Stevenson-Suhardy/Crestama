package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Set;

@Entity(name="product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "section")
    private Collection<Section> sections;
}
