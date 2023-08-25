package com.crestama.crestamawebsite.service.user;

import com.crestama.crestamawebsite.entity.User;

import java.util.List;

public interface IUser {
    List<User> findAll();

    User findById(Long id);

    User save(User user);

    void deleteById(Long id);
}
