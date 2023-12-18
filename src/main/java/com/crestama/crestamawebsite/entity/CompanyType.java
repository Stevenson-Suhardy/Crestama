package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class CompanyType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="type")
    private String type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "companyType")
    private Set<SalesReportForm> salesReportForms = new HashSet<>();

    public Set<SalesReportForm> getSalesReportForms() {
        return salesReportForms;
    }

    public void setSalesReportForms(Set<SalesReportForm> salesReportForms) {
        this.salesReportForms = salesReportForms;
    }

    public CompanyType() {}

    public CompanyType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
