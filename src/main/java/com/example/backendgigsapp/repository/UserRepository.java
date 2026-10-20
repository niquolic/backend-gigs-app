package com.example.backendgigsapp.repository;

import com.example.backendgigsapp.entity.UsersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UsersEntity, String> {

    Optional<UsersEntity> findByLogin(String login);
}
