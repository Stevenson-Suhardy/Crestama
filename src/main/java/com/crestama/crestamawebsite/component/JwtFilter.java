package com.crestama.crestamawebsite.component;

import com.crestama.crestamawebsite.service.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
@Order(1)
public class JwtFilter extends OncePerRequestFilter {
    private CustomUserDetailService userDetailService;
    private TokenManager tokenManager;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Set<String> excludedUrls = new HashSet<>(
            Arrays.asList("/", "/about", "/login", "/processLogin",
                    "/register", "/styles/**", "/assets/**", "/js/**", "/fonts/**")
    );

    @Autowired
    public JwtFilter(CustomUserDetailService userDetailService, TokenManager tokenManager) {
        super();
        this.userDetailService = userDetailService;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (excludedUrls.stream().anyMatch(url -> pathMatcher.match(url, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            token = tokenHeader.substring(7);

            try {
                username = tokenManager.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (ExpiredJwtException e) {
                response.sendRedirect("/refreshToken");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            if (tokenManager.validateJwtToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}