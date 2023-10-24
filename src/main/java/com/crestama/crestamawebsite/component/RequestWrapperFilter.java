package com.crestama.crestamawebsite.component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestWrapperFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // Getting session
        HttpSession session = request.getSession(true);

        String token = (String) session.getAttribute("token");

        RequestHeaderWrapper requestHeaderWrapper = new RequestHeaderWrapper(request);

        requestHeaderWrapper.addHeader("Authorization", token);
        filterChain.doFilter(requestHeaderWrapper, response);
    }
}
