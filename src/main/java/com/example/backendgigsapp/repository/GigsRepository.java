package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.GigsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface GigsRepository extends MongoRepository<GigsEntity, String> {

    @Query("{'userId': ?0}")
    List<GigsEntity> findByUserIdOrderByDateDesc(String userId);
}

