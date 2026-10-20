package com.example.backendgigsapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Génération et vérification du JWT, centralisées ici (utilisé par le login
 * pour générer, et par JwtAuthFilter pour vérifier sur chaque requête).
 *
 * Réécrit pour l'API jjwt 0.13.0 : l'ancienne API 0.9.1 signait avec
 * SignatureAlgorithm + une String brute ; la nouvelle API veut une vraie
 * SecretKey, et HS512 exige une clé d'au moins 64 octets (sinon jjwt lève
 * une WeakKeyException). Pour que n'importe quelle valeur de JWT_SECRET
 * fonctionne sans que tu aies à te soucier de sa longueur exacte, on en
 * dérive une clé de 64 octets via SHA-512 plutôt que d'utiliser la chaîne
 * brute telle quelle.
 */
@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // 30 jours en millisecondes par défaut (voir application.properties).
    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    private SecretKey signingKey() {
        try {
            // SHA-512 produit toujours 64 octets, exactement le minimum
            // requis par HS512 : n'importe quelle valeur de JWT_SECRET,
            // quelle que soit sa longueur, donne donc une clé valide.
            byte[] digest = MessageDigest.getInstance("SHA-512")
                    .digest(jwtSecret.getBytes(StandardCharsets.UTF_8));
            return Keys.hmacShaKeyFor(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-512 non disponible", e);
        }
    }

    public String generateToken(String login, String userId) {
        Date now = new Date();
        return Jwts.builder()
                .subject(login)
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(signingKey())
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
