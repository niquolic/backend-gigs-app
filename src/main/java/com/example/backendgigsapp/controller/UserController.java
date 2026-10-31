package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.dto.UserPreferencesResponse;
import com.example.backendgigsapp.entity.UserPreferencesEntity;
import com.example.backendgigsapp.request.UserPreferencesRequest;
import com.example.backendgigsapp.request.UserRequest;
import com.example.backendgigsapp.service.UserPreferencesService;
import com.example.backendgigsapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final UserPreferencesService userPreferencesService;

    public UserController(UserService userService, UserPreferencesService userPreferencesService) {
        this.userService = userService;
        this.userPreferencesService = userPreferencesService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        userService.logout(authorization.substring(7));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest) {
        userService.register(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully. Please log in to your account.");
    }

    @GetMapping("/user/preferences")
    public UserPreferencesResponse getPreferences(Authentication authentication) {
        UserPreferencesEntity preferences = userPreferencesService.getForUser(authentication.getName());
        return new UserPreferencesResponse(preferences.getLanguage(), preferences.getCurrency());
    }

    @PutMapping("/user/preferences")
    public UserPreferencesResponse updatePreferences(
            @Valid @RequestBody UserPreferencesRequest request,
            Authentication authentication) {
        UserPreferencesEntity preferences = userPreferencesService.updateForUser(
                authentication.getName(),
                request.language(),
                request.currency()
        );
        return new UserPreferencesResponse(preferences.getLanguage(), preferences.getCurrency());
    }
}
