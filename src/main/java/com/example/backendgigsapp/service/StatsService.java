package com.example.backendgigsapp.service;

import com.example.backendgigsapp.dto.BandStatsPageResponse;
import com.example.backendgigsapp.dto.GigStatsResponse;
import com.example.backendgigsapp.entity.StatsBandsEntity;
import com.example.backendgigsapp.entity.StatsCountryEntity;
import com.example.backendgigsapp.repository.StatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class StatsService {

    private static final int BAND_PAGE_SIZE = 5;

    private final StatsRepository statsRepository;

    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Cacheable(cacheNames = "bandStats", key = "#p0 + ':' + #p1")
    public BandStatsPageResponse getStatsOfUser(String userId, int page) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative.");
        }

        long offset = (long) page * BAND_PAGE_SIZE;
        List<StatsBandsEntity> results = statsRepository.findBandsByUserId(
                userId,
                offset,
                BAND_PAGE_SIZE + 1
        );

        boolean hasNext = results.size() > BAND_PAGE_SIZE;
        List<StatsBandsEntity> bands = hasNext
                ? List.copyOf(results.subList(0, BAND_PAGE_SIZE))
                : List.copyOf(results);

        return new BandStatsPageResponse(bands, page, BAND_PAGE_SIZE, hasNext);
    }

    @Cacheable(cacheNames = "gigStats", key = "#p0")
    public GigStatsResponse getGigStats(String userId) {
        Date[] bounds = currentYearBounds();

        return new GigStatsResponse(
                statsRepository.findAllByUserId(userId),
                statsRepository.findByYearEqual(bounds[0], bounds[1], userId),
                statsRepository.findTotalPrice(userId),
                statsRepository.findPriceThisYear(bounds[0], bounds[1], userId)
        );
    }

    @Cacheable(cacheNames = "countryStats", key = "#p0")
    public List<StatsCountryEntity> getCountryStatsOfUser(String userId) {
        return statsRepository.findTop5CountryByUserId(userId);
    }

    private Date[] currentYearBounds() {
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate endDate = startDate.plusYears(1);
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return new Date[]{start, end};
    }
}
