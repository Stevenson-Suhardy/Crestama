package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.role.RoleService;
import com.crestama.crestamawebsite.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public String listOfUsers(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user/listOfUsers";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());

        return "user/userForm";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());

        return "user/userForm";
    }

    @Transactional
    @PostMapping("/save")
    public String save(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());

            return "user/userForm";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.save(user);
        return "redirect:/users/users";
    }

    @GetMapping("/deleteUser/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteById(id);

        return "redirect:/users/users";
    }
}
