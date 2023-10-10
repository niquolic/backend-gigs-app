package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.StatsBandsEntity;
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

}
