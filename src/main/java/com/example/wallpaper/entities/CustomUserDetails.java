package com.example.wallpaper.entities;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities or roles here (e.g., user.getRoles())
        return Collections.emptyList(); // Update this if you add roles in the User entity
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Customize based on business logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Customize based on business logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Customize based on business logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Or `user.isEnabled()` if you add an `enabled` field
    }

    public User getUser() {
        return user;
    }
}
