package com.example.wallpaper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.wallpaper.repositories")
public class WallpaperApplication {

	public static void main(String[] args) {
		SpringApplication.run(WallpaperApplication.class, args);
	}

}
