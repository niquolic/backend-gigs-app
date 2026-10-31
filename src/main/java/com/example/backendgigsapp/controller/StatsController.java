package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.dto.BandStatsPageResponse;
import com.example.backendgigsapp.dto.GigStatsResponse;
import com.example.backendgigsapp.entity.StatsCountryEntity;
import com.example.backendgigsapp.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/getStatsOfUser")
    public BandStatsPageResponse getStatsOfUser(
            @RequestParam(defaultValue = "0") int page,
            Authentication authentication) {
        return statsService.getStatsOfUser(authentication.getName(), page);
    }

    @GetMapping("/getGigStats")
    public GigStatsResponse getGigStats(Authentication authentication) {
        return statsService.getGigStats(authentication.getName());
    }

    @GetMapping("/getCountryStatsOfUser")
    public List<StatsCountryEntity> getCountryStatsOfUser(Authentication authentication) {
        return statsService.getCountryStatsOfUser(authentication.getName());
    }
}
