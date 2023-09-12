package com.crestama.crestamawebsite.controller;

import com.crestama.crestamawebsite.entity.User;
import com.crestama.crestamawebsite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String listOfUsers(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user/listOfUsers";
    }

    @GetMapping("/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());

        return "user/userForm";
    }

    @GetMapping("/editUser")
    public String editUser(Model model, @RequestParam("userId") Long id) {
        model.addAttribute("user", userService.findById(id));

        return "user/userForm";
    }

    @Transactional
    @PostMapping("/save")
    public String save(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users/users";
    }

    @GetMapping("/deleteUser")
    public String delete(@RequestParam("userId") Long id) {
        userService.deleteById(id);

        return "redirect:/users/users";
    }
}
