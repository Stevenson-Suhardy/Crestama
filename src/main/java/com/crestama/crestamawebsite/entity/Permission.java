package com.crestama.crestamawebsite.entity;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;
}
