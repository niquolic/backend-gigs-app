package com.example.backendgigsapp.service;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.repository.GigsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceGigs {

    @Autowired
    GigsRepository gigsRepo;

     public List<GigsEntity> getGigsByUserId(Long id){
         return gigsRepo.findByUserIdOrderByDateDesc(id);
     }

     public Optional<GigsEntity> getGigById(Long id) {
         return gigsRepo.findById(id);
     }

}
