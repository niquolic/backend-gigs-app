package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.entities.StatsBandsEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StatsRepository extends MongoRepository <GigsEntity, Long>{

    //@Query("{'userId': ?0}")
    @Aggregation(pipeline = {
            "{$match: {'userId': ?0}}",
            "{$unwind: '$bands' }",
            "{$group: { _id: '$bands', count: { $sum: 1 } } }",
            "{$sort: {count: -1}}",
            "{$limit: 5}"
    })
    List<StatsBandsEntity> findTop5BandsByUserId(String userId);

}
