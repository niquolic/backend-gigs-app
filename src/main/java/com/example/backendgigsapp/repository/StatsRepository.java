package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.StatsBandsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface StatsRepository extends MongoRepository <StatsBandsEntity, Long>{

    @Query("{'id': ?0}")
    List<StatsBandsEntity> findTop5BandsByUserId(String id);

}
