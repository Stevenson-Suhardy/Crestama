package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

@Entity(name="section_image")
public class SectionImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="image_path")
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;
}
