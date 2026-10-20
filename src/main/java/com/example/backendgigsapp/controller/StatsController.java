package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.entity.StatsBandsEntity;
import com.example.backendgigsapp.entity.StatsCountryEntity;
import com.example.backendgigsapp.service.ServiceStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
public class StatsController {

    @Autowired
    private ServiceStats serviceStats;

    @GetMapping("/getStatsOfUser")
    public List<StatsBandsEntity> getStatsOfUser(Authentication authentication) {
        return serviceStats.getStatsOfUser(authentication.getName());
    }

    @GetMapping("/getTotalNumberOfGigs")
    public Long getTotalNumberOfGigs(Authentication authentication) {
        return serviceStats.getTotalNumberOfGigs(authentication.getName());
    }

    @GetMapping("/getTotalNumberOfGigsThisYear")
    public Long getTotalNumberOfGigsThisYear(Authentication authentication) {
        Date[] bounds = currentYearBounds();
        return serviceStats.getTotalNumberOfGigsThisYear(bounds[0], bounds[1], authentication.getName());
    }

    @GetMapping("/getCountryStatsOfUser")
    public List<StatsCountryEntity> getCountryStatsOfUser(Authentication authentication) {
        return serviceStats.getCountryStatsOfUser(authentication.getName());
    }

    @GetMapping("/getTotalPrice")
    public Double getTotalPrice(Authentication authentication) {
        return serviceStats.getTotalPrice(authentication.getName());
    }

    @GetMapping("/getPriceThisYear")
    public Double getPriceThisYear(Authentication authentication) {
        Date[] bounds = currentYearBounds();
        return serviceStats.getPriceThisYear(bounds[0], bounds[1], authentication.getName());
    }

    private Date[] currentYearBounds() {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = startDate.plusYears(1);
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new Date[]{start, end};
    }
}
