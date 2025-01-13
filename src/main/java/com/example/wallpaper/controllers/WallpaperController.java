package com.example.wallpaper.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.wallpaper.dto.ApiResponse;
import com.example.wallpaper.entities.User;
import com.example.wallpaper.entities.Wallpaper;
import com.example.wallpaper.services.S3Service;
import com.example.wallpaper.services.UserService;
import com.example.wallpaper.services.WallpaperService;

@RestController
@RequestMapping("/api/wallpaper")
public class WallpaperController {

    @Autowired
    private WallpaperService wallpaperService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private UserService userService;

    @GetMapping("getWallpaper/{id}")
    // Fetch a wallpaper
    public ResponseEntity<ApiResponse<Wallpaper>> getWallpaper (@PathVariable Long id) {
        try {
            Wallpaper wallpaper = wallpaperService.getWallpaperById(id);
            return ResponseEntity.ok(new ApiResponse<>(true,"Wallpaper Fetched Successfully",wallpaper));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, e.getMessage()));
        }
    }

    @PostMapping("/uploadWallpaper")
    // Upload a wallpaper
    public ResponseEntity<ApiResponse<String>> uploadWallpaper(
        @RequestParam("file") MultipartFile file,
        @RequestParam("description") String description,
        @RequestParam("username") String username
    ) {
        try {
            Optional<User> user = userService.findUserByUsername(username);
            if (!user.isPresent()) {
                return ResponseEntity.status(404).body(new ApiResponse<>(false, "This user does not exist"));
            }
            User myUser = user.get();
            String fileName = s3Service.uploadFile(file);
            Wallpaper wallpaper = new Wallpaper();
            wallpaper.setDescription(description);
            wallpaper.setTitle(fileName);
            wallpaper.setUser(myUser);
            wallpaper.setImageurl("example");
            wallpaperService.uploadWallpaper(wallpaper);
            return ResponseEntity.ok().body(new ApiResponse<>(true, "Wallpaper uploaded successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(false, "Failed to upload wallpaper: " + e.getMessage()));
        }
    }
}
