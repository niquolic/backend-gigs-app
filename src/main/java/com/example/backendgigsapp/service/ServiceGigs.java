package com.example.backendgigsapp.service;

import com.example.backendgigsapp.entity.GigsEntity;
import com.example.backendgigsapp.repository.GigsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ServiceGigs {

    @Autowired
    private GigsRepository gigsRepo;

    public List<GigsEntity> getGigsByUserId(String userId) {
        return gigsRepo.findByUserIdOrderByDateDesc(userId, Sort.by(Sort.Order.desc("date")));
    }

    /**
     * Récupère un concert en vérifiant qu'il appartient bien à userId.
     * Avant ce fix, getGigById/editGig/deleteGig ne vérifiaient jamais ça :
     * n'importe quel utilisateur connecté pouvait lire/modifier/supprimer
     * le concert de n'importe qui d'autre en devinant son id (un ObjectId
     * Mongo n'est pas secret, juste peu probable à deviner au hasard, ce
     * qui n'est pas une protection).
     */
    public GigsEntity getOwnedGig(String id, String userId) {
        GigsEntity gig = gigsRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Concert introuvable"));
        assertOwnership(gig, userId);
        return gig;
    }

    public GigsEntity addGig(GigsEntity gig, String userId) {
        // userId vient du token vérifié, jamais de ce que le client a mis
        // dans le corps de la requête.
        gig.setUserId(userId);
        return gigsRepo.save(gig);
    }

    public void deleteGig(String id, String userId) {
        GigsEntity gig = getOwnedGig(id, userId);
        gigsRepo.delete(gig);
    }

    public GigsEntity editGig(GigsEntity gig, String userId) {
        GigsEntity existing = getOwnedGig(gig.getId(), userId);
        gig.setUserId(existing.getUserId()); // le propriétaire ne peut jamais changer
        return gigsRepo.save(gig);
    }

    private void assertOwnership(GigsEntity gig, String userId) {
        if (!gig.getUserId().equals(userId)) {
            // Même exception/message que "introuvable" : on ne révèle pas
            // à un utilisateur qu'un concert appartenant à quelqu'un
            // d'autre existe avec cet id.
            throw new NoSuchElementException("Concert introuvable");
        }
    }
}
