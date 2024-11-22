package com.example.wallpaper.entities;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "userotp")
@Getter
@Setter
public class UserOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int otp;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public boolean isExpired() {
        return createdAt.plusMinutes(5).isBefore(LocalDateTime.now());
    }
    
}
