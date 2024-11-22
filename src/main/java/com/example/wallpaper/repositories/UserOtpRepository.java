package com.example.wallpaper.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallpaper.entities.UserOtp;

@Repository
public interface UserOtpRepository extends JpaRepository<UserOtp,Long> {
    Optional<UserOtp> findByEmail(String email);
}
