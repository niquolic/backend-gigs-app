package com.example.backendgigsapp.repository;

import com.example.backendgigsapp.entity.UserPreferencesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserPreferencesRepository extends MongoRepository<UserPreferencesEntity, String> {

    Optional<UserPreferencesEntity> findByUserId(org.bson.types.ObjectId userId);
}
