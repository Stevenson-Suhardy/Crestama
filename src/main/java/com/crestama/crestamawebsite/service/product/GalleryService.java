package com.crestama.crestamawebsite.service.product;

import com.crestama.crestamawebsite.entity.Gallery;
import com.crestama.crestamawebsite.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class GalleryService implements IGalleryService {
    private GalleryRepository galleryRepository;

    @Autowired
    public GalleryService(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    // Overridden methods
    @Override
    public List<Gallery> findAll() {
        return galleryRepository.findAll();
    }

    @Override
    public Gallery findById(Long id) {
        Optional<Gallery> result = galleryRepository.findById(id);
        Gallery gallery = null;

        if (result.isPresent()) {
            gallery = result.get();
        }
        else {
            throw new RuntimeException("Did not find product id - " + id);
        }

        return gallery;
    }

    @Override
    @Transactional
    public Gallery save(Gallery gallery) {
        return galleryRepository.save(gallery);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        galleryRepository.deleteById(id);
    }

    @Override
    public Page<Gallery> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Gallery> list;

        if (findAll().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, findAll().size());
            list = findAll().subList(startItem, toIndex);
        }

        return new PageImpl<Gallery>(list, PageRequest.of(currentPage, pageSize), findAll().size());
    }
}
