package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="city_name")
    private String cityName;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "city")
    private Set<SalesReportForm> salesReportForms = new HashSet<>();

    public Set<SalesReportForm> getSalesReportForms() {
        return salesReportForms;
    }

    public void setSalesReportForms(Set<SalesReportForm> salesReportForms) {
        this.salesReportForms = salesReportForms;
    }

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
