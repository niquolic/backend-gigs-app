package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.StatsEntity;
import com.example.backendgigsapp.entities.UsersEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface StatsRepository {

    @Query("{'id': ?0}")
    Optional<StatsEntity> findById(Long id);

}
