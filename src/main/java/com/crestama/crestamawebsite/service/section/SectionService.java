package com.crestama.crestamawebsite.service.section;

import com.crestama.crestamawebsite.entity.Section;
import com.crestama.crestamawebsite.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService implements ISection {
    private SectionRepository sectionRepository;

    @Autowired
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Section findById(Long id) {
        Optional<Section> result = sectionRepository.findById(id);

        Section section = null;

        if (result.isPresent()) {
            section = result.get();
        }
        else {
            throw new RuntimeException("Did not find section id - " + id);
        }
        return section;
    }

    @Override
    @Transactional
    public Section save(Section section) {
        return sectionRepository.save(section);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sectionRepository.deleteById(id);
    }
}
