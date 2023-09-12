package com.crestama.crestamawebsite.config;

import com.crestama.crestamawebsite.component.CustomAuthenticationProvider;
import com.crestama.crestamawebsite.component.JwtFilter;
import com.crestama.crestamawebsite.component.TokenManager;
import com.crestama.crestamawebsite.service.CustomUserDetailService;
import com.crestama.crestamawebsite.service.refreshToken.RefreshTokenService;
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
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private UserService userService;
    private JwtFilter filter;
    private CustomUserDetailService customUserDetailService;
    private TokenManager tokenManager;

    private RefreshTokenService refreshTokenService;
    @Autowired
    public SecurityConfig(
            UserService userService,
            JwtFilter filter,
            CustomUserDetailService customUserDetailService,
            TokenManager tokenManager,
            RefreshTokenService refreshTokenService
    ) {
        this.userService = userService;
        this.filter = filter;
        this.customUserDetailService = customUserDetailService;
        this.tokenManager = tokenManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/roleHierarchy").hasRole("STAFF")
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/products/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
        ).formLogin(form ->
                form.loginPage("/login")
                    .loginProcessingUrl("/processLogin")
                    .permitAll()
        ).logout(logout ->
                logout.permitAll()
        );
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_STAFF \n ROLE_STAFF > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(new CustomAuthenticationProvider(
                        userService, encoder(),
                        customUserDetailService, tokenManager, refreshTokenService
                ))
                .build();
    }

}
