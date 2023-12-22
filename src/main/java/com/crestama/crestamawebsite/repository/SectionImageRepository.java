package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.SectionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionImageRepository extends JpaRepository<SectionImage, Long> {
}
