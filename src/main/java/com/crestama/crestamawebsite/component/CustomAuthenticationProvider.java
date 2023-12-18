package com.crestama.crestamawebsite.component;

//import com.crestama.crestamawebsite.entity.RefreshToken;
import com.crestama.crestamawebsite.entity.Role;
import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.CustomUserDetailService;
//import com.crestama.crestamawebsite.service.refreshToken.RefreshTokenService;
import com.crestama.crestamawebsite.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    // Private members
    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private CustomUserDetailService customUserDetailService;
    private TokenManager tokenManager;
//    private RefreshTokenService refreshTokenService;

    /**
     * Autowire Services
     * @param userService
     * @param passwordEncoder
     * @param customUserDetailService
     * @param tokenManager
     * @param refreshTokenService
     */
    @Autowired
    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder,
                                        CustomUserDetailService customUserDetailService,
                                        TokenManager tokenManager
                                        ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailService = customUserDetailService;
        this.tokenManager = tokenManager;
    }

    /**
     * Overriding authenticate method
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        User tempUser = userService.findByEmail(authentication.getName());

        if (tempUser != null) {
            if (passwordEncoder.matches(authentication.getCredentials().toString(), tempUser.getPassword())) {
                // Granting Role to User
                List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
                for (Role role : tempUser.getRoles()) {
                    grantedAuthorityList.add(new SimpleGrantedAuthority(role.getName()));
                }

                // Delete existing refresh token in the database
//                if (refreshTokenService.findByUserId(tempUser.getId()) != null) {
//                    refreshTokenService.deleteByUserId(tempUser.getId());
//                }

                // Getting session
                ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
                HttpSession session = attr.getRequest().getSession(false);

                // Generating JWT Token storing the user details
                UserDetails userDetails = customUserDetailService.loadUserByUsername(tempUser.getEmail());
                String token = tokenManager.generateJwtToken(userDetails);

                // Generating Refresh Token and saving it to the database
//                RefreshToken refreshToken = refreshTokenService.save(
//                        refreshTokenService.createRefreshToken(tempUser.getId())
//                );

                // Setting session attributes
                session.setAttribute("token", "Bearer " + token);

                return new UsernamePasswordAuthenticationToken(
                        tempUser.getEmail(), tempUser.getPassword(), grantedAuthorityList
                );
            }
        }
        return null;
    }

    /**
     * Set the authentication class
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
