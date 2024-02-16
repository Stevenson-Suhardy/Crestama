package com.crestama.crestamawebsite.entity;
import com.crestama.crestamawebsite.utility.S3Util;
import jakarta.persistence.*;

import static com.crestama.crestamawebsite.utility.S3Util.sectionImageFolderURL;

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

    public SectionImage() {
    }

    public SectionImage(String imagePath, Section section) {
        this.imagePath = imagePath;
        this.section = section;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Transient
    public String getImagePath() {
        if (imagePath == null || id == null) {
            return null;
        }

        return S3Util.sectionImageFolderURL + id + "/" + imagePath;
    }

    public String getImageName() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }
}
