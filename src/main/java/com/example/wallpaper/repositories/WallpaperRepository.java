package com.example.wallpaper.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.wallpaper.entities.Wallpaper;

@Repository
public interface WallpaperRepository extends JpaRepository<Wallpaper, Long> {
    List<Wallpaper> findByUserId(Long userId);
}
