package com.example.wallpaper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtp {
    private String email;
    private int otp;
}
