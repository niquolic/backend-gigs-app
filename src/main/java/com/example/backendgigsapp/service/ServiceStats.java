package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.StatsEntity;
import com.example.backendgigsapp.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceStats {

    @Autowired
    private StatsRepository statsRepository;

    public StatsEntity getStatsOfUser(Long id){
        return statsRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No stats found"));
    }

}
