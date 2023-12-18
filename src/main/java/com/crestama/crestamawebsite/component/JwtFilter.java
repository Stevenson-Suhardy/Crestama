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
    // Private members
    private CustomUserDetailService userDetailService;
    private TokenManager tokenManager;

    // Private final members
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Set<String> excludedUrls = new HashSet<>(
            Arrays.asList("/", "/about", "/login", "/processLogin",
                    "/register", "/styles/**", "/assets/**", "/js/**", "/fonts/**")
    );

    /**
     * Autowiring Services
     * @param userDetailService
     * @param tokenManager
     */
    @Autowired
    public JwtFilter(CustomUserDetailService userDetailService, TokenManager tokenManager) {
        super();
        this.userDetailService = userDetailService;
        this.tokenManager = tokenManager;
    }

    /**
     * Overrides doFilterInternal method. This method will check if JWT exists inside the session storage.
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (excludedUrls.stream().anyMatch(url -> pathMatcher.match(url, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        // Get the token from the request header
        String tokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;
        // Checks if it starts with 'Bearer' or it is null
        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            // Gets the token
            token = tokenHeader.substring(7);

            try {
                // Gets the username from the token
                username = tokenManager.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            catch (ExpiredJwtException e) {
                response.sendRedirect("/login");
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        // If username is not null and session does not have authentication
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Gets the user details from the username
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            // Validates the token
            if (tokenManager.validateJwtToken(token, userDetails)) {
                // Get the authorities
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                // Create the authentication token
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication for security
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Pass the request, and response
        filterChain.doFilter(request, response);
    }
}