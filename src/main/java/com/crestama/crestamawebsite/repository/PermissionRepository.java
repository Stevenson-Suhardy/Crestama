package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query(value="SELECT * FROM Permission WHERE name=?", nativeQuery = true)
    Permission findByName(String name);
}
