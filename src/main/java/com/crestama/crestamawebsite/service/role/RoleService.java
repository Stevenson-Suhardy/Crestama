package com.crestama.crestamawebsite.service.role;

import com.crestama.crestamawebsite.dao.RoleRepository;
import com.crestama.crestamawebsite.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRole {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findById(Long id) {
        Optional<Role> result = roleRepository.findById(id);

        Role role = null;
        if (result.isPresent()) {
            role = result.get();
        }
        else {
            throw new RuntimeException("Did not find permission id - " + id);
        }
        return role;
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}