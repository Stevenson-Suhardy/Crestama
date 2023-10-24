package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProspectRepository extends JpaRepository<Prospect, Long> {
}
