package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.role.RoleService;
import com.crestama.crestamawebsite.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;

@Controller
public class RegisterController {
    private UserService userService;

    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());

        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistration(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/registration-form";
        }

        User existingUser = userService.findByEmail(user.getEmail());

        if (existingUser != null) {
            model.addAttribute("user", new User());
            model.addAttribute("error", "Email address has already been taken.");

            return "redirect:/registration-form";
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEnabled(true);
        user.setRoles(Arrays.asList(roleService.findByName("ROLE_USER")));

        userService.save(user);

        session.setAttribute("user", user);
        return "redirect:/";
    }
}
