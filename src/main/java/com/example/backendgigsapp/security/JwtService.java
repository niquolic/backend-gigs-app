package com.example.backendgigsapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    private final SecretKey signingKey;
    private final long jwtExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expirationMs:900000}") long jwtExpirationMs) {
        if (jwtSecret == null || jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException(
                    "jwt.secret must contain at least 32 bytes of random data. " +
                    "Configure a strong secret in your secret manager.");
        }
        if (jwtExpirationMs <= 0) {
            throw new IllegalStateException("jwt.expirationMs must be greater than zero.");
        }
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String generateToken(String login, String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(login)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
