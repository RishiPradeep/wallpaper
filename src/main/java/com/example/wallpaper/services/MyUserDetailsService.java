package com.example.wallpaper.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.wallpaper.entities.CustomUserDetails;
import com.example.wallpaper.entities.User;
import com.example.wallpaper.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user.get());
    }

}
