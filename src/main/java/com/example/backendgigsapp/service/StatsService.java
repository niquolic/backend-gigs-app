package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.StatsBandsEntity;
import com.example.backendgigsapp.entity.StatsCountryEntity;
import com.example.backendgigsapp.repository.StatsRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class StatsService {

    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public List<StatsBandsEntity> getStatsOfUser(String userId) {
        return statsRepository.findTop5BandsByUserId(userId);
    }

    public Long getTotalNumberOfGigs(String userId) {
        return statsRepository.findAllByUserId(userId);
    }

    public Long getTotalNumberOfGigsThisYear(String userId) {
        Date[] bounds = currentYearBounds();
        return statsRepository.findByYearEqual(bounds[0], bounds[1], userId);
    }

    public List<StatsCountryEntity> getCountryStatsOfUser(String userId) {
        return statsRepository.findTop5CountryByUserId(userId);
    }

    public Double getTotalPrice(String userId) {
        return statsRepository.findTotalPrice(userId);
    }

    public Double getPriceThisYear(String userId) {
        Date[] bounds = currentYearBounds();
        return statsRepository.findPriceThisYear(bounds[0], bounds[1], userId);
    }

    private Date[] currentYearBounds() {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = startDate.plusYears(1);
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new Date[]{start, end};
    }
}
