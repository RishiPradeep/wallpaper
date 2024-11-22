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

import com.example.wallpaper.dto.ApiResponse;
import com.example.wallpaper.dto.LoginRequest;
import com.example.wallpaper.dto.VerifyOtp;
import com.example.wallpaper.entities.User;
import com.example.wallpaper.services.EmailService;
import com.example.wallpaper.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping
    // Create a user
    public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "User created successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to create user: " + e.getMessage()));
        }
    }

    @GetMapping
    // Get a list of all users
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse<>(true, "Users fetched successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to fetch users: " + e.getMessage()));
        }
    }


    @GetMapping("/{username}")
    // Get user by username
    public ResponseEntity<ApiResponse<User>> getUserByUsername (@PathVariable String username) {
        return userService.findUserByUsername(username)
            .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User fetched successfully",user)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false, "User not found")));
    }

    @PostMapping("/login")
    // Login user
    public ResponseEntity<ApiResponse<Void>> loginUser (@RequestBody LoginRequest loginRequest) {
        boolean isValidLogin = userService.loginUser(loginRequest.getUsername(),loginRequest.getPassword());
        if (isValidLogin) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Login success"));
        } else {
            return ResponseEntity.status(401).body(new ApiResponse<>(false, "Incorrect username or password"));
        }
    }

    @GetMapping("/getOtp/{email}")
    // Send OTP
    public ResponseEntity<ApiResponse<Void>> sendOtp (@PathVariable String email) {
        try {
            emailService.sendOtp(email);
            return ResponseEntity.ok().body(new ApiResponse<>(true, "OTP sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to send OTP: " + e.getMessage()));
        }
    }

    @PostMapping("/verifyOtp")
    // Verify OTP
    public ResponseEntity<ApiResponse<Void>>  verifyOtp (@RequestBody VerifyOtp verifyOtp) {
        try {
            boolean check = emailService.verifyOtp(verifyOtp.getEmail(), verifyOtp.getOtp());
            if (check) {
                return ResponseEntity.ok().body(new ApiResponse<>(true, "Otp verified successfully"));
            }
            return ResponseEntity.status(401).body(new ApiResponse<>(false,"OTP is expired or invalid"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to verify OTP: " + e.getMessage()));
        }
    }
}
