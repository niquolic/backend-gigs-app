package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.UsersEntity;
import com.example.backendgigsapp.repository.UserRepository;
import com.example.backendgigsapp.request.UserRequest;
import com.example.backendgigsapp.security.JwtService;
import com.example.backendgigsapp.security.RevokedTokenService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final UserPreferencesService userPreferencesService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RevokedTokenService revokedTokenService;

    public UserService(UserRepository userRepo, UserPreferencesService userPreferencesService,
                       PasswordEncoder passwordEncoder, JwtService jwtService,
                       RevokedTokenService revokedTokenService) {
        this.userRepo = userRepo;
        this.userPreferencesService = userPreferencesService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.revokedTokenService = revokedTokenService;
    }

    public String login(UserRequest request) {
        UsersEntity user = userRepo.findByLogin(request.login().trim())
                .orElseThrow(() -> new NoSuchElementException("Invalid credentials."));

        if (!passwordMatches(request.password(), user)) {
            throw new NoSuchElementException("Invalid credentials.");
        }
        return jwtService.generateToken(user.getLogin(), user.getId());
    }

    public void logout(String token) {
        revokedTokenService.revoke(token);
    }

    /**
     * Existing plaintext passwords are upgraded after a successful login.
     * New passwords are always stored using the configured password encoder.
     */
    private boolean passwordMatches(String rawPassword, UsersEntity user) {
        String stored = user.getPassword();
        if (isBcryptHash(stored)) {
            return passwordEncoder.matches(rawPassword, stored);
        }
        if (stored != null && stored.equals(rawPassword)) {
            user.setPassword(passwordEncoder.encode(rawPassword));
            userRepo.save(user);
            return true;
        }
        return false;
    }

    private boolean isBcryptHash(String value) {
        return value != null
                && (value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    public void register(UserRequest request) {
        String login = request.login().trim();
        if (userRepo.findByLogin(login).isPresent()) {
            throw new IllegalArgumentException("That login is already in use. Please choose another.");
        }

        UsersEntity user = new UsersEntity();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(request.password()));

        UsersEntity savedUser;
        try {
            savedUser = userRepo.save(user);
        } catch (DuplicateKeyException exception) {
            // The unique MongoDB index also protects concurrent registrations.
            throw new IllegalArgumentException("That login is already in use. Please choose another.");
        }

        try {
            userPreferencesService.createForUser(
                    savedUser.getId(),
                    request.language(),
                    request.currency()
            );
        } catch (RuntimeException exception) {
            // Avoid leaving an account without its required preferences document.
            userRepo.delete(savedUser);
            throw exception;
        }
    }
}
