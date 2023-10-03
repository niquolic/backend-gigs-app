package com.example.backendgigsapp.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.backendgigsapp.service.ServiceStats;
import com.example.backendgigsapp.entities.StatsEntity;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StatsController {

    @Autowired
    private ServiceStats serviceStats;

    @GetMapping("/getStatsOfUser")
    public ResponseEntity getStatsOfUser(@RequestParam Long id){
        try{
            StatsEntity statsEntity = serviceStats.getStatsOfUser(id);
            return ResponseEntity.ok(statsEntity);
        }
        catch (NoSuchElementException e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
}
