package com.example.wallpaper.services;

import com.example.wallpaper.entities.Wallpaper;
import com.example.wallpaper.repositories.WallpaperRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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

    public Wallpaper getWallpaperById(Long id) {
        Optional<Wallpaper> optionalWallpaper =  wallpaperRepository.findById(id);
        if (optionalWallpaper.isPresent()) {
            return optionalWallpaper.get();
        } else {
            throw new NoSuchElementException("Wallpaper with id" + id + " not found");
        }
    }
}
