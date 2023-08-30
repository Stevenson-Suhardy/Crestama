package com.crestama.crestamawebsite.service.permission;

import com.crestama.crestamawebsite.repository.PermissionRepository;
import com.crestama.crestamawebsite.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermission {
    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission findById(Long id) {
        Optional<Permission> result = permissionRepository.findById(id);

        Permission permission = null;
        if (result.isPresent()) {
            permission = result.get();
        }
        else {
            throw new RuntimeException("Did not find permission id - " + id);
        }
        return permission;
    }

    @Override
    public Permission findByName(String name) {
        return permissionRepository.findByName(name);
    }

    @Override
    @Transactional
    public Permission save(Permission permission) {
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        permissionRepository.deleteById(id);
    }
}
