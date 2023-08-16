package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.UsersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UsersEntity, String> {

    // Search in the database the user by login and password
    @Query("{'login': ?0, 'password': ?1}")
    Optional<UsersEntity> findByLoginAndPassword(String login, String password);

}
