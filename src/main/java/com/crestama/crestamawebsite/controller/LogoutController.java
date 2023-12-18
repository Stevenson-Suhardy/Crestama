package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.User;
//import com.crestama.crestamawebsite.service.refreshToken.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LogoutController {
//    private RefreshTokenService refreshTokenService;
    public LogoutController() {}

    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/logout")
    public String logout(HttpSession session, Authentication authentication,
                         HttpServletRequest request, HttpServletResponse response) {
        // Deleting User Refresh Token
        User user = (User) session.getAttribute("user");

        this.logoutHandler.logout(request, response, authentication);
        session.invalidate();
        return "redirect:/";
    }
}
