package com.example.backendgigsapp.dto;

public record GigStatsResponse(
        Long totalNumberOfGigs,
        Long totalNumberOfGigsThisYear,
        Double totalPrice,
        Double priceThisYear
) {
}
