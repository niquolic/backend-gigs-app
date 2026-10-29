package com.example.backendgigsapp.controller;

import com.example.backendgigsapp.dto.GigRequest;
import com.example.backendgigsapp.dto.GigResponse;
import com.example.backendgigsapp.service.GigsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GigsController {

    private final GigsService gigsService;

    public GigsController(GigsService gigsService) {
        this.gigsService = gigsService;
    }

    @GetMapping("/getGigsByUserId")
    public List<GigResponse> getGigsByUserId(Authentication authentication) {
        return gigsService.getGigsByUserId(authentication.getName());
    }

    @PostMapping("/addGigToList")
    public ResponseEntity<GigResponse> addGigToList(
            @Valid @RequestBody GigRequest request,
            Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(gigsService.addGig(request, authentication.getName()));
    }

    @DeleteMapping("/deleteGig")
    public ResponseEntity<Void> deleteGig(@RequestParam String id, Authentication authentication) {
        gigsService.deleteGig(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getGigById")
    public GigResponse getGigById(@RequestParam String id, Authentication authentication) {
        return gigsService.getGigById(id, authentication.getName());
    }

    @PostMapping("/editGig")
    public GigResponse editGig(@Valid @RequestBody GigRequest request, Authentication authentication) {
        return gigsService.editGig(request, authentication.getName());
    }
}
