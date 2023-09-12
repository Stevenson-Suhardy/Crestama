package com.crestama.crestamawebsite.service.refreshToken;

import com.crestama.crestamawebsite.entity.RefreshToken;
import com.crestama.crestamawebsite.exception.TokenRefreshException;
import com.crestama.crestamawebsite.repository.RefreshTokenRepository;
import com.crestama.crestamawebsite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshToken {
    private RefreshTokenRepository refreshTokenRepository;
    private UserService userService;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository,
                               UserService userService
                               ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
    }

    @Override
    public RefreshToken findById(Long id) {
        Optional<RefreshToken> result = refreshTokenRepository.findById(id);

        RefreshToken refreshToken = null;

        if (result.isPresent()) {
            refreshToken = result.get();
        }
        else {
            throw new RuntimeException("Did not find refresh token id - " + id);
        }

        return refreshToken;
    }

    @Override
    public List<RefreshToken> findAll() {
        return refreshTokenRepository.findAll();
    }

    @Override
    public RefreshToken findByUserId(Long id) {
        return refreshTokenRepository.findByUserId(id);
    }

    @Override
    @Transactional
    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long id) {
        long refreshTokenDurationMs = 86400000L;
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userService.findById(id));

        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        return save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    @Transactional
    public void deleteByUserId(Long id) {
        refreshTokenRepository.deleteByUser(userService.findById(id));
    }


}
