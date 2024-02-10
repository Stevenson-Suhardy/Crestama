package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "prospect")
public class Prospect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "prospect")
    private Set<SalesReportForm> salesReportForms = new HashSet<>();

    public Set<SalesReportForm> getSalesReportForms() {
        return salesReportForms;
    }

    public void setSalesReportForms(Set<SalesReportForm> salesReportForms) {
        this.salesReportForms = salesReportForms;
    }

    public Prospect() {};

    public Prospect(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
