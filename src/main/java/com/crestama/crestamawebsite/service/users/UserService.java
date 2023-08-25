package com.crestama.crestamawebsite.service.users;

import com.crestama.crestamawebsite.dao.UserRepository;
import com.crestama.crestamawebsite.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUser {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Overridden methods

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> result = userRepository.findById(username);

        User user = null;

        if (result.isPresent()) {
            user = result.get();
        }
        return user;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteByUsername(String username) {
        userRepository.deleteById(username);
    }
}
