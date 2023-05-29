package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.service.ServiceGigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GigsController {

    @Autowired
    ServiceGigs serviceGigs;

    @GetMapping("/getGigsByUserId")
    public List<GigsEntity> getGigsByUserId(@RequestParam() Long id){
        return serviceGigs.getGigsByUserId(id);
    }

}
