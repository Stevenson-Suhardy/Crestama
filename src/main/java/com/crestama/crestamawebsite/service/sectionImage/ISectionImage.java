package com.crestama.crestamawebsite.service.sectionImage;

import com.crestama.crestamawebsite.entity.Section;
import com.crestama.crestamawebsite.entity.SectionImage;

import java.util.List;

public interface ISectionImage {
    List<SectionImage> findAll();

    SectionImage findById(Long id);

    SectionImage save(SectionImage sectionImage);

    void deleteById(Long id);
}
