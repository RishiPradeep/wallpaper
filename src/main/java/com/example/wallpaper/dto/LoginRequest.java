package com.example.wallpaper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    //TODO: ADD VALIDATORS
    private String username;
    private String password;
}
