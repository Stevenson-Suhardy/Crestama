package com.crestama.crestamawebsite.service.product;

import com.crestama.crestamawebsite.entity.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGalleryService {
    List<Gallery> findAll();

    Gallery findById(Long id);

    Gallery save(Gallery gallery);

    void deleteById(Long id);

    Page<Gallery> findPaginated(Pageable pageable);
}
