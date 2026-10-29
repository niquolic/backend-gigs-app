package com.example.backendgigsapp.dto;

import java.time.LocalDate;
import java.util.List;

public record GigResponse(
        String id,
        String userId,
        List<String> bands,
        LocalDate date,
        String city,
        String country,
        String venue,
        Double price
) {}
