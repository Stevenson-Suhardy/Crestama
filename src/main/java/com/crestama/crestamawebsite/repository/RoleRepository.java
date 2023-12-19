package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value="SELECT * FROM role WHERE name=?", nativeQuery = true)
    Role findByName(String name);
}
