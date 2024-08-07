package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.StatsBandsEntity;
import com.example.backendgigsapp.entities.StatsCountryEntity;
import com.example.backendgigsapp.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ServiceStats {

    @Autowired
    private StatsRepository statsRepository;

    public List<StatsBandsEntity> getStatsOfUser(String userId){
        return statsRepository.findTop5BandsByUserId(userId);
    }

    public Long getTotalNumberOfGigs(String userId){
        return statsRepository.findAllByUserId(userId);
    }

    public Long getTotalNumberOfGigsThisYear(Date start, Date end, String userId){
        return statsRepository.findByYearEqual(start, end, userId);
    }

    public List<StatsCountryEntity> getCountryStatsOfUser(String userId){
        return statsRepository.findTop5CountryByUserId(userId);
    }

    public Double getTotalPrice(String userId){
        return statsRepository.findTotalPrice(userId);
    }

    public Double getPriceThisYear(Date start, Date end, String userId){
        return statsRepository.findPriceThisYear(start, end, userId);
    }

}
