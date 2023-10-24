package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

@Entity
public class CompanyType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="type")
    private String type;

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
