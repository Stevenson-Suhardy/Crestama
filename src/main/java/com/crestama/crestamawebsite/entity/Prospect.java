package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

@Entity
public class Prospect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    public Prospect() {};

    public Prospect(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
