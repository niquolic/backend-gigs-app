package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.repository.GigsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceGigs {

    @Autowired
    private GigsRepository gigsRepo;

    public List<GigsEntity> getGigsByUserId(String userId) {
        return gigsRepo.findByUserIdOrderByDateDesc(userId, Sort.by(Sort.Order.desc("date")));
    }

    public Optional<GigsEntity> getGigById(String id) {
        return gigsRepo.findById(id);
    }

    public GigsEntity addGig(GigsEntity gig) {
        return gigsRepo.save(gig);
    }

    public boolean deleteGig(String id) {
        if (gigsRepo.existsById(id)) {
            gigsRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public boolean editGig(GigsEntity gig) {
        if (gigsRepo.existsById(gig.getId())) {
            System.out.println(gig.getPrice());
            gigsRepo.save(gig);
            return true;
        } else {
            return false;
        }
    }
}
