package com.example.wallpaper.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(boolean success, String message) {
        this.message = message;
        this.success = success;
    }

    public ApiResponse(boolean success, String message, T data) {
        this.data = data;
        this.message = message;
        this.success = success;
    }
}
