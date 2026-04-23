package com.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.inventory.entity.User;
import com.inventory.service.UserService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        User u = service.login(user.getUsername(), user.getPassword());

        if (u != null) {
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }
}