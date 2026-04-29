package com.inventory.service;

import com.inventory.exception.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.inventory.entity.User;
import com.inventory.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;

    private BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    // REGISTER
    public User register(User user) {

        // Check duplicate username
        User existingUser = repo.findByUsername(user.getUsername());

        if (existingUser != null) {
            throw new DuplicateUserException("Username already exists");
        }

        // Hash password before saving
        user.setPassword(
                encoder.encode(user.getPassword())
        );

        return repo.save(user);
    }

    // LOGIN
    public User login(String username, String password) {

        User user = repo.findByUsername(username);

        if (user != null &&
                encoder.matches(password, user.getPassword())) {

            return user;
        }

        return null;
    }
}