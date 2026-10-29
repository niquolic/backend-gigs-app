package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.entity.StatsBandsEntity;
import com.example.backendgigsapp.entity.StatsCountryEntity;
import com.example.backendgigsapp.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/getStatsOfUser")
    public List<StatsBandsEntity> getStatsOfUser(Authentication authentication) {
        return statsService.getStatsOfUser(authentication.getName());
    }

    @GetMapping("/getTotalNumberOfGigs")
    public Long getTotalNumberOfGigs(Authentication authentication) {
        return statsService.getTotalNumberOfGigs(authentication.getName());
    }

    @GetMapping("/getTotalNumberOfGigsThisYear")
    public Long getTotalNumberOfGigsThisYear(Authentication authentication) {
        return statsService.getTotalNumberOfGigsThisYear(authentication.getName());
    }

    @GetMapping("/getCountryStatsOfUser")
    public List<StatsCountryEntity> getCountryStatsOfUser(Authentication authentication) {
        return statsService.getCountryStatsOfUser(authentication.getName());
    }

    @GetMapping("/getTotalPrice")
    public Double getTotalPrice(Authentication authentication) {
        return statsService.getTotalPrice(authentication.getName());
    }

    @GetMapping("/getPriceThisYear")
    public Double getPriceThisYear(Authentication authentication) {
        return statsService.getPriceThisYear(authentication.getName());
    }
}
