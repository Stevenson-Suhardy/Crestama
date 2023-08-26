package com.crestama.crestamawebsite.dao;

import com.crestama.crestamawebsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user WHERE email=?", nativeQuery = true)
    User findByEmail(String email);
}
