package com.crestama.crestamawebsite.service.section;

import com.crestama.crestamawebsite.entity.Section;

import java.util.List;

public interface ISection {
    List<Section> findAll();

    Section findById(Long id);

    Section save(Section section);

    void deleteById(Long id);
}
