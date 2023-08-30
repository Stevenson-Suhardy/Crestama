package com.crestama.crestamawebsite.repository;

import com.crestama.crestamawebsite.entity.RefreshToken;
import com.crestama.crestamawebsite.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query(value = "SELECT * FROM RefreshToken WHERE user_id = ?", nativeQuery = true)
    RefreshToken findByUserId(Long id);

    @Modifying
    int deleteByUser(User user);
}
