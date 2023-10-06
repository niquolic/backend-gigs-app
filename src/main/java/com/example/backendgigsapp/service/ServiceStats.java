package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.StatsBandsEntity;
import com.example.backendgigsapp.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceStats {

    @Autowired
    private StatsRepository statsRepository;

    public List<StatsBandsEntity> getStatsOfUser(String id){
        return statsRepository.findTop5BandsByUserId(id);
    }

}
