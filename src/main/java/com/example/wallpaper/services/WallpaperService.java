package com.example.wallpaper.services;

import com.example.wallpaper.entities.Wallpaper;
import com.example.wallpaper.repositories.WallpaperRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WallpaperService {

    @Autowired
    private WallpaperRepository wallpaperRepository;

    public Wallpaper uploadWallpaper(Wallpaper wallpaper) {
        return wallpaperRepository.save(wallpaper);
    }

    public List<Wallpaper> getWallpapersByUserId(Long userId) {
        return wallpaperRepository.findByUserId(userId);
    }
}
