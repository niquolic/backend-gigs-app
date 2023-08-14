package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.service.ServiceGigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class GigsController {

    @Autowired
    private ServiceGigs serviceGigs;

    @GetMapping("/getGigsByUserId")
    public List<GigsEntity> getGigsByUserId(@RequestParam Long userId) {
        return serviceGigs.getGigsByUserId(userId);
    }

    @PostMapping("/addGigToList")
    public GigsEntity addGigToList(@RequestBody GigsEntity gig) {
        // Ajouter les validations ou la logique métier supplémentaire si nécessaire
        return serviceGigs.addGig(gig);
    }

    @PostMapping("/deleteGig")
    public ResponseEntity<String> deleteGig(@RequestBody String id) {
        if (serviceGigs.deleteGig(id)) {
            return ResponseEntity.ok("Concert supprimé");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getGigById")
    public Optional<GigsEntity> getGigById(@RequestParam String id) {
        return serviceGigs.getGigById(id);
    }

    @PostMapping("/editGig")
    public ResponseEntity<String> editGig(@RequestBody GigsEntity gig) {
        if (serviceGigs.editGig(gig)) {
            return ResponseEntity.ok("Concert modifié");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
