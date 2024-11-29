package com.example.wallpaper.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

@Service
public class JWTService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 86400000; //1 DAY

    private SecretKey getSigningKey() {
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

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                   .verifyWith(getSigningKey())
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


}
