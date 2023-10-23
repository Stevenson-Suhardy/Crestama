package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

@Entity
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="cityName")
    private String cityName;

    public City() {}

    public City(String cityName) {
        this.cityName = cityName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
