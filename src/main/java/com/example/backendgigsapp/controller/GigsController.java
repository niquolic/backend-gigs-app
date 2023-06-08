package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.repository.GigsRepository;
import com.example.backendgigsapp.service.ServiceGigs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class GigsController {

    private final GigsRepository gigsRepository;

    public GigsController(GigsRepository gigsRepository) {
        this.gigsRepository = gigsRepository;
    }

    @Autowired
    ServiceGigs serviceGigs;

    @GetMapping("/getGigsByUserId")
    public List<GigsEntity> getGigsByUserId(@RequestParam() Long id){
        return serviceGigs.getGigsByUserId(id);
    }

    @PostMapping("/addGigToList")
    public GigsEntity addGigToList(@RequestBody GigsEntity gig) {
        // Ajouter les validations ou la logique métier supplémentaire si nécessaire
        return gigsRepository.save(gig);
    }

    @PostMapping("/deleteGig")
    public ResponseEntity <String> deleteGig(@RequestBody long id){
        if (gigsRepository.existsById(id)) {
            gigsRepository.deleteById(id);
            return ResponseEntity.ok("Concert supprimé");
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getGigById")
    public Optional<GigsEntity> getGigById(@RequestParam Long id){
        System.out.println("id:" + id);
        return serviceGigs.getGigById(id);
    }

    @PostMapping("/editGig")
    public ResponseEntity <String> editGig(@RequestBody GigsEntity gig){
        if (gigsRepository.existsById(gig.getId())){
            gigsRepository.save(gig);
            return ResponseEntity.ok("Concert modifié");
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
