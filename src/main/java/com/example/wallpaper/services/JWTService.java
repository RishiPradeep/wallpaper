package com.example.wallpaper.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 86400000; //1 DAY

    private Key getSigningKey() {
        if (SECRET_KEY == null | SECRET_KEY.isEmpty()) {
            throw new IllegalStateException("Secret Key not configured properly");
        } else {
            return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        }
    }

    public String generateToken(String username) {
        Map<String,Object> claims = new HashMap<>();
        Key key = getSigningKey();
        return Jwts.builder()
                   .claims()
                   .add(claims)
                   .subject(username)
                   .issuedAt(new Date(System.currentTimeMillis()))
                   .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                   .and()
                   .signWith(key)
                   .compact();
    }

}
