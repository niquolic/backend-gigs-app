package com.example.backendgigsapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Login is required")
        @Size(max = 100, message = "Login must not exceed 100 characters")
        String login,

        @NotBlank(message = "Password is required")
        @Size(min = 10, max = 72, message = "Password must be between 10 and 72 characters")
        String password
) {}
