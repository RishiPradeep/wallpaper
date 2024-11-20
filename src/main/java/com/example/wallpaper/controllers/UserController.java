package com.example.wallpaper.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wallpaper.dto.LoginRequest;
import com.example.wallpaper.entities.User;
import com.example.wallpaper.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername (@PathVariable String username) {
        return userService.findUserByUsername(username)
            .map(user -> ResponseEntity.ok(user))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser (@RequestBody LoginRequest loginRequest) {
        boolean isValidLogin = userService.loginUser(loginRequest.getUsername(),loginRequest.getPassword());
        if (isValidLogin) {
            return ResponseEntity.ok("Login Success");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}
