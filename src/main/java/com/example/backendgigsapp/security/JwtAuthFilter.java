package com.example.backendgigsapp.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Avant ce fix : AUCUN endpoint (à part /login et /register) ne vérifiait
 * quoi que ce soit. N'importe qui pouvait appeler /getGigsByUserId?userId=X
 * pour n'importe quel X, sans même être connecté. Ce filtre lit le token
 * envoyé par le client (header "Authorization: Bearer <token>"), vérifie
 * sa signature/expiration, et attache l'identité correspondante à la
 * requête. Les controllers n'ont ensuite plus jamais à faire confiance à un
 * userId fourni par le client : ils utilisent celui du token, vérifié ici.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtService.parseClaims(token);
                String userId = claims.get("userId", String.class);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // Token absent, invalide ou expiré : on laisse la requête
                // non authentifiée. C'est SecurityConfig qui décide ensuite
                // si la route demandée exige une authentification (401 sinon).
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}
