package com.example.backendgigsapp.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserPreferencesRequest(
        @NotBlank
        @Pattern(regexp = "^(fr|en)$")
        String language,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{3}$")
        String currency
) {
}
