package com.crestama.crestamawebsite.config;

import com.crestama.crestamawebsite.component.*;
import com.crestama.crestamawebsite.service.CustomUserDetailService;
import com.crestama.crestamawebsite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserService userService;
    private JwtFilter jwtFilter;
    private RequestWrapperFilter requestWrapperFilter;
    private CustomUserDetailService customUserDetailService;
    private TokenManager tokenManager;
    @Autowired
    public SecurityConfig(
            UserService userService,
            JwtFilter jwtFilter,
            RequestWrapperFilter requestWrapperFilter,
            CustomUserDetailService customUserDetailService,
            TokenManager tokenManager
    ) {
        this.userService = userService;
        this.jwtFilter = jwtFilter;
        this.requestWrapperFilter = requestWrapperFilter;
        this.customUserDetailService = customUserDetailService;
        this.tokenManager = tokenManager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/roleHierarchy").hasRole("ADMIN")
                        .requestMatchers("/salesForm/**").hasRole("SALES")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/gallery/**").hasRole("STAFF")
                        .requestMatchers("/manage").hasAnyRole("SALES", "STAFF")
                        .anyRequest().permitAll()
        ).formLogin(form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/processLogin")
                    .permitAll()
        ).logout(logout ->
                logout.permitAll()
        );
        http.addFilterBefore(jwtFilter, ExceptionTranslationFilter.class);
        http.addFilterBefore(requestWrapperFilter, JwtFilter.class);
        http.exceptionHandling((exception) -> exception
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedPage("/error/access-denied"));
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_ADMIN > ROLE_SALES \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(new CustomAuthenticationProvider(
                        userService, encoder(),
                        customUserDetailService, tokenManager
                ))
                .build();
    }

}
