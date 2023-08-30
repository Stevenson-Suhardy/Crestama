package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.component.TokenManager;
import com.crestama.crestamawebsite.entity.RefreshToken;
import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.exception.TokenRefreshException;
import com.crestama.crestamawebsite.service.CustomUserDetailService;
import com.crestama.crestamawebsite.service.refreshToken.RefreshTokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class AuthController {
    private RefreshTokenService refreshTokenService;
    private CustomUserDetailService customUserDetailService;
    private TokenManager tokenManager;

    @Autowired
    public AuthController(
            RefreshTokenService refreshTokenService,
            CustomUserDetailService customUserDetailService,
            TokenManager tokenManager
    ) {
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailService = customUserDetailService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/refreshToken")
    public String refreshToken(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        RefreshToken refreshToken = refreshTokenService.findByUserId(loggedInUser.getId());

        String newToken = refreshTokenService.findByToken(refreshToken.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    UserDetails userDetails = customUserDetailService.loadUserByUsername(user.getEmail());
                    return tokenManager.generateJwtToken(userDetails);
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken.getToken(), "Refresh token is not in database!"));

        session.removeAttribute("token");
        session.setAttribute("token", newToken);

        return "redirect:/";
    }
}
