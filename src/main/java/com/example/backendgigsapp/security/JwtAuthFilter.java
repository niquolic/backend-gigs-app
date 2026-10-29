package com.example.backendgigsapp.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * This filter validates the bearer token, verifies its signature and expiration,
 * and attaches the authenticated user ID to the request security context.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;

    public JwtAuthFilter(JwtService jwtService, RevokedTokenService revokedTokenService) {
        this.jwtService = jwtService;
        this.revokedTokenService = revokedTokenService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (revokedTokenService.isRevoked(token)) {
                SecurityContextHolder.clearContext();
            } else {
                try {
                    Claims claims = jwtService.parseClaims(token);
                    String userId = claims.get("userId", String.class);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (Exception exception) {
                    // Invalid or expired tokens leave the request unauthenticated.
                    SecurityContextHolder.clearContext();
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
