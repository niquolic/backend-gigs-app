package com.example.backendgigsapp.security;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Keeps logged-out JWTs revoked until they expire.
 *
 * This in-memory store is suitable for a single application instance. A
 * multi-instance deployment should use a shared persistent revocation store.
 */
@Service
public class RevokedTokenService {

    private final JwtService jwtService;
    private final ConcurrentMap<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    public RevokedTokenService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public void revoke(String token) {
        Claims claims = jwtService.parseClaims(token);
        Instant expiresAt = claims.getExpiration().toInstant();
        revokedTokens.put(token, expiresAt);
        removeExpiredTokens();
    }

    public boolean isRevoked(String token) {
        Instant expiresAt = revokedTokens.get(token);
        if (expiresAt == null) {
            removeExpiredTokens();
            return false;
        }
        if (!expiresAt.isAfter(Instant.now())) {
            revokedTokens.remove(token, expiresAt);
            return false;
        }
        return true;
    }

    private void removeExpiredTokens() {
        Instant now = Instant.now();
        revokedTokens.entrySet().removeIf(entry -> !entry.getValue().isAfter(now));
    }
}
