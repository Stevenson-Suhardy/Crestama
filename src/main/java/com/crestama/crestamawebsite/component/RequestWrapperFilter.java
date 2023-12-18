package com.crestama.crestamawebsite.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestWrapperFilter extends OncePerRequestFilter {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final Set<String> excludedUrls = new HashSet<>(
            Arrays.asList("/", "/about", "/login", "/processLogin", "/register", "/styles/**", "/assets/**")
    );
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (excludedUrls.stream().anyMatch(url -> pathMatcher.match(url, request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return;
        }
        // Getting session
        HttpSession session = request.getSession(false);
        String token = "";

        if (session != null && session.getAttribute("token") != null) {
            token = (String) session.getAttribute("token");
        }

        RequestHeaderWrapper requestHeaderWrapper = new RequestHeaderWrapper(request);

        requestHeaderWrapper.addHeader("Authorization", token);
        filterChain.doFilter(requestHeaderWrapper, response);
    }
}
