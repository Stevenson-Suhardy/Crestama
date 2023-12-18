package com.crestama.crestamawebsite.service.role;

import com.crestama.crestamawebsite.entity.Role;

import java.util.List;

public interface IRole {
    List<Role> findAll();

    Role findById(Long id);

    Role findByName(String name);

    Role save(Role role);

    void deleteById(Long id);
}
