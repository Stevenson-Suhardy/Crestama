package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

}
