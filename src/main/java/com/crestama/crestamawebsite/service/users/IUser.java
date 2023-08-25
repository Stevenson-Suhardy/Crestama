package com.crestama.crestamawebsite.service.users;

import com.crestama.crestamawebsite.entity.User;

import java.util.List;

public interface IUser {
    List<User> findAll();

    User findByUsername(String username);

    User save(User user);

    void deleteByUsername(String username);
}
