package com.crestama.crestamawebsite.service.permission;

import com.crestama.crestamawebsite.entity.Permission;
import com.crestama.crestamawebsite.entity.Product;

import java.util.List;

public interface IPermission {
    List<Permission> findAll();

    Permission findById(Long id);

    Permission findByName(String name);

    Permission save(Permission permission);

    void deleteById(Long id);
}
