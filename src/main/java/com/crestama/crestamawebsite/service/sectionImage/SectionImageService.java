package com.crestama.crestamawebsite.service.sectionImage;

import com.crestama.crestamawebsite.entity.SectionImage;
import com.crestama.crestamawebsite.repository.SectionImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class SectionImageService implements ISectionImage {
    private SectionImageRepository sectionImageRepository;

    @Autowired
    public SectionImageService(SectionImageRepository sectionImageRepository) {
        this.sectionImageRepository = sectionImageRepository;
    }

    @Override
    public List<SectionImage> findAll() {
        return sectionImageRepository.findAll();
    }

    @Override
    public SectionImage findById(Long id) {
        Optional<SectionImage> result = sectionImageRepository.findById(id);

        SectionImage sectionImage = null;

        if (result.isPresent()) {
            sectionImage = result.get();
        }
        else {
            throw new RuntimeException("Did not find section image id - " + id);
        }
        return sectionImage;
    }

    @Override
    @Transactional
    public SectionImage save(SectionImage sectionImage) {
        return sectionImageRepository.save(sectionImage);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sectionImageRepository.deleteById(id);
    }
}
