package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.StatsCountryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backendgigsapp.service.ServiceStats;
import com.example.backendgigsapp.entities.StatsBandsEntity;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StatsController {

    @Autowired
    private ServiceStats serviceStats;

    @GetMapping("/getStatsOfUser")
    public ResponseEntity getStatsOfUser(@RequestParam String userId){
        try{
            List<StatsBandsEntity> statsEntity = serviceStats.getStatsOfUser(userId);
            return ResponseEntity.ok(statsEntity);
        }
        catch (NoSuchElementException e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getTotalNumberOfGigs")
    public ResponseEntity getTotalNumberOfGigs(@RequestParam String userId){
        try{
            Long numberOfGigs = serviceStats.getTotalNumberOfGigs(userId);
            return ResponseEntity.ok(numberOfGigs);
        }
        catch (NoSuchElementException e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getTotalNumberOfGigsThisYear")
    public ResponseEntity getTotalNumberOfGigsThisYear(@RequestParam String userId){
        try{
            // Récupération de la date
            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = LocalDate.of(currentDate.getYear(), 1, 1);
            LocalDate endDate = startDate.plusYears(1);

            Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Long numberOfGigsThisYear = serviceStats.getTotalNumberOfGigsThisYear(start, end, userId);
            return ResponseEntity.ok(numberOfGigsThisYear);
        }
        catch (NoSuchElementException e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getCountryStatsOfUser")
    public ResponseEntity getCountryStatsOfUserId(@RequestParam String userId){
        try{
            List<StatsCountryEntity> countryEntity = serviceStats.getCountryStatsOfUser(userId);
            return ResponseEntity.ok(countryEntity);
        }
        catch (NoSuchElementException e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}
