package com.example.backendgigsapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.util.List;

/**
 * HTTP input DTO. Ownership is deliberately not accepted from the client;
 * the service always takes the user ID from the authenticated principal.
 */
public record GigRequest(
        String id,
        @NotEmpty(message = "At least one band is required") List<@NotBlank String> bands,
        @NotNull(message = "A date is required") LocalDate date,
        @NotBlank(message = "A city is required") String city,
        @NotBlank(message = "A country is required") String country,
        @NotBlank(message = "A venue is required") String venue,
        @PositiveOrZero(message = "Price must be zero or greater") Double price
) {}
