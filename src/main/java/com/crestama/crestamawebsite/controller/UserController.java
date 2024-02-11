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

        return "user/updateUser";
    }

    @GetMapping("/changePassword/{id}")
    public String changePassword(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id));

        return "user/changePassword";
    }

    @Transactional
    @PostMapping("/save")
    public String save(@ModelAttribute @Valid User user,
                       BindingResult result,
                       @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());

            return "user/userForm";
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("emailError", "Email Address already exists");

            return "user/userForm";
        }

        if (confirmPassword != null) {
            if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
                model.addAttribute("roles", roleService.findAll());
                model.addAttribute("confirmError",
                        "Confirm Password is required.");

                return "user/userForm";
            }
            else {
                if (!user.getPassword().equals(confirmPassword)) {
                    model.addAttribute("roles", roleService.findAll());
                    model.addAttribute("confirmError",
                            "Password does not match with Confirm Password");

                    return "user/userForm";
                }
            }
        }
        else {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("confirmError",
                    "Confirm Password is required.");
            return "user/userForm";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);

        userService.save(user);
        return "redirect:/users/users";
    }

    @Transactional
    @PostMapping("/update")
    public String update(@ModelAttribute @Valid User user,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());

            return "user/updateUser";
        }

        User tempUser = userService.findById(user.getId());
        User searchUser = userService.findByEmail(user.getEmail());

        if (searchUser != null) {
            if (!searchUser.getEmail().equals(tempUser.getEmail())) {
                model.addAttribute("roles", roleService.findAll());
                model.addAttribute("emailError", "Email Address already exists");

                return "user/updateUser";
            }
        }

        // Set password and enabled status
        user.setPassword(tempUser.getPassword());
        user.setEnabled(tempUser.isEnabled());

        userService.save(user);

        return "redirect:/users/users";
    }

    @Transactional
    @PostMapping("/updatePassword")
    public String updatePassword(@ModelAttribute @Valid User user,
                                 BindingResult result,
                                 @RequestParam("confirmPassword") String confirmPassword, Model model) {
        if (result.hasErrors()) {
            return "user/changePassword";
        }

        if (confirmPassword != null) {
            if (confirmPassword.isEmpty() || confirmPassword.isBlank()) {
                model.addAttribute("roles", roleService.findAll());
                model.addAttribute("confirmError",
                        "Confirm New Password is required.");

                return "user/changePassword";
            }
            else {
                if (!user.getPassword().equals(confirmPassword)) {
                    model.addAttribute("roles", roleService.findAll());
                    model.addAttribute("confirmError",
                            "New Password does not match with Confirm New Password");

                    return "user/changePassword";
                }
            }
        }
        else {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("confirmError",
                    "Confirm New Password is required.");
            return "user/changePassword";
        }

        String password = user.getPassword();

        // Copy existing data into user and change the password to match new password
        user = userService.findById(user.getId());
        user.setPassword(passwordEncoder.encode(password));

        userService.save(user);

        return "redirect:/users/users";
    }

    @GetMapping("/disableUser/{id}")
    public String disableUser(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setEnabled(false);
        userService.save(user);

        return "redirect:/users/users";
    }

    @GetMapping("/enableUser/{id}")
    public String enableUser(@PathVariable Long id) {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.save(user);

        return "redirect:/users/users";
    }
}
