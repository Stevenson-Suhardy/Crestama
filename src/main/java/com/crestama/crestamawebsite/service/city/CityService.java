package com.crestama.crestamawebsite.service.city;

import com.crestama.crestamawebsite.entity.City;
import com.crestama.crestamawebsite.entity.SalesReportForm;
import com.crestama.crestamawebsite.repository.CityRepository;
import com.crestama.crestamawebsite.repository.SalesReportFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService implements ICity {
    private CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public City findById(Long id) {
        Optional<City> result = cityRepository.findById(id);

        City city = null;

        if (result.isPresent()) {
            city = result.get();
        }
        else {
            throw new RuntimeException("Did not find city id - " + id);
        }

        return city;
    }
}
