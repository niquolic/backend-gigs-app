package com.example.backendgigsapp.dto;

import com.example.backendgigsapp.entity.StatsBandsEntity;

import java.util.List;

public record BandStatsPageResponse(
        List<StatsBandsEntity> content,
        int page,
        int size,
        boolean hasNext
) {
}
