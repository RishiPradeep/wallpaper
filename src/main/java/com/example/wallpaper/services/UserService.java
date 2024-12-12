package com.example.wallpaper.services;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.wallpaper.entities.User;
import com.example.wallpaper.entities.Wallpaper;
import com.example.wallpaper.repositories.UserRepository;


import java.util.List;
@Service
public class UserService {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder(11);



    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
         if (optionalUser.isPresent()) {
             User user = optionalUser.get();
             for (Wallpaper wallpaper : user.getWallpapers()) {
                 wallpaper.setImageurl(s3Service.generatePresignedUrl(wallpaper.getTitle()));
             }
         }
        return optionalUser;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean loginUser (String username, String password) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
