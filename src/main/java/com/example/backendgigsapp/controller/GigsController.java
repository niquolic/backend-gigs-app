package com.example.backendgigsapp.controller;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.service.ServiceGigs;
import com.example.backendgigsapp.service.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {
    "https://angular-frontend-513726246973.europe-west1.run.app",
    "http://localhost:4200"
})
public class GigsController {

    @Autowired
    private ServiceGigs serviceGigs;

    @Autowired
    private ServiceUser userService;

    @GetMapping("/getGigsByUserId")
    public List<GigsEntity> getGigsByUserId(@RequestParam String userId) {
        return serviceGigs.getGigsByUserId(userId);
    }

    @PostMapping("/addGigToList")
    public GigsEntity addGigToList(@RequestBody GigsEntity gig, @RequestParam String userId) {
        GigsEntity savedGig = serviceGigs.addGig(gig);

        // Récupérer l'ObjectId du gig inséré
        String gigObjectId = savedGig.getId();

        // Appeler la méthode addGigsToUser avec l'ObjectId en tant que paramètre
        // Assurez-vous d'avoir une référence à votre service utilisateur ici
        userService.addGigsToUser(gigObjectId, userId);

        return savedGig;
    }

    @PostMapping("/deleteGig")
    public ResponseEntity<String> deleteGig(@RequestBody String id, @RequestParam String userId) {
        if (serviceGigs.deleteGig(id)) {
            userService.deleteGig(userId,id);
            if(userService.deleteGig(userId,id)) {
                return ResponseEntity.ok("Concert supprimé");
            } else {
                return ResponseEntity.notFound().build();
            }
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
