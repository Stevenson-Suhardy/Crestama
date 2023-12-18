package com.crestama.crestamawebsite.service.city;

import com.crestama.crestamawebsite.entity.City;

import java.util.List;

public interface ICity {
    List<City> findAll();

    City findById(Long id);
}
