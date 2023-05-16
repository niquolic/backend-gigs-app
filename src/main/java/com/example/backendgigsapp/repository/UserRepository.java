package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.UsersEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UsersEntity, Long> {

    // Search in the database the user by login and password
    Optional<UsersEntity> findByLoginAndPassword(String login, String password);

}
