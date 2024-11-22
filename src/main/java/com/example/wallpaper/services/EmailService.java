package com.example.wallpaper.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.wallpaper.entities.User;
import com.example.wallpaper.entities.UserOtp;

import com.example.wallpaper.repositories.UserOtpRepository;
import com.example.wallpaper.repositories.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    public JavaMailSender mailSender;

    @Autowired
    private UserOtpRepository userOtpRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body,true);
        mailSender.send(message);
    }

    public void sendOtp(String email) throws MessagingException {
        // Check if the user exists first
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User with email " + email + " not found");
        }
        Optional<UserOtp> optionalUserOtp = userOtpRepository.findByEmail(email);
        if (optionalUserOtp.isPresent()) {
            UserOtp existingOtp = optionalUserOtp.get();
            if (!existingOtp.isExpired()) {
                throw new IllegalStateException("OTP already sent and is still valid");
            } else {
                userOtpRepository.delete(existingOtp);
            }
        }
        int newOtp = new Random().nextInt(900000) + 100000;
        UserOtp userOtp = new UserOtp();
        userOtp.setOtp(newOtp);
        userOtp.setEmail(email);
        userOtp.setCreatedAt(LocalDateTime.now());
        userOtpRepository.save(userOtp);
        String subject = "Your OTP for BackdropVault";
        String body = "<p>Your OTP is <strong>" + newOtp + "</strong>.</p>"
        + "<p>It will expire in 5 minutes.</p>";
        sendEmail(email, subject, body);
    }

    public boolean verifyOtp (String email, int otp) {
        Optional<UserOtp> optionalUserOtp = userOtpRepository.findByEmail(email);
        if (!optionalUserOtp.isPresent()) {
            return false;
        }
        UserOtp userOtp = optionalUserOtp.get();
        if (userOtp.isExpired()) {
            return false;
        }
        if (userOtp.getOtp() != otp) {
            return false;
        }
        if (userOtp.getOtp() == otp) {
            return true;
        }
        return false;
    }
}
