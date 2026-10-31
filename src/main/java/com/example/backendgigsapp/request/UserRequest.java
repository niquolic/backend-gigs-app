package com.example.backendgigsapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "Login is required")
        @Size(max = 100, message = "Login must not exceed 100 characters")
        String login,

        @NotBlank(message = "Password is required")
        @Size(min = 10, max = 72, message = "Password must be between 10 and 72 characters")
        String password,

        @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Language must be a valid language code, such as en or fr-FR")
        String language,

        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a three-letter uppercase code, such as EUR or USD")
        String currency
) {}
