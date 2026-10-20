package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.entity.GigsEntity;
import com.example.backendgigsapp.service.ServiceGigs;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GigsController {

    @Autowired
    private ServiceGigs serviceGigs;

    // Le userId vient de authentication.getName(), rempli par JwtAuthFilter
    // à partir du token vérifié. Le ?userId= encore envoyé par l'app est
    // simplement ignoré côté serveur.
    @GetMapping("/getGigsByUserId")
    public List<GigsEntity> getGigsByUserId(Authentication authentication) {
        return serviceGigs.getGigsByUserId(authentication.getName());
    }

    @PostMapping("/addGigToList")
    public GigsEntity addGigToList(@RequestBody @Valid GigsEntity gig, Authentication authentication) {
        return serviceGigs.addGig(gig, authentication.getName());
    }

    @PostMapping("/deleteGig")
    public ResponseEntity<String> deleteGig(@RequestBody String id, Authentication authentication) {
        serviceGigs.deleteGig(id, authentication.getName());
        return ResponseEntity.ok("Concert supprimé");
    }

    @GetMapping("/getGigById")
    public GigsEntity getGigById(@RequestParam String id, Authentication authentication) {
        return serviceGigs.getOwnedGig(id, authentication.getName());
    }

    @PostMapping("/editGig")
    public ResponseEntity<String> editGig(@RequestBody @Valid GigsEntity gig, Authentication authentication) {
        serviceGigs.editGig(gig, authentication.getName());
        return ResponseEntity.ok("Concert modifié");
    }
}
