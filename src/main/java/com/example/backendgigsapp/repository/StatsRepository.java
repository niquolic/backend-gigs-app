package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.GigsEntity;
import com.example.backendgigsapp.entities.StatsBandsEntity;
import com.example.backendgigsapp.entities.StatsCountryEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Date;
import java.util.List;

public interface StatsRepository extends MongoRepository <GigsEntity, Long>{

    @Aggregation(pipeline = {
            "{$match: {'userId': ?0}}",
            "{$unwind: '$bands' }",
            "{$group: { _id: '$bands', count: { $sum: 1 } } }",
            "{$sort: {count: -1}}",
            "{$limit: 5}"
    })
    List<StatsBandsEntity> findTop5BandsByUserId(String userId);

    @Aggregation(pipeline = {
            "{$match:  {'userId': ?0}}",
            "{$group:  { _id:  'id', count:  { $sum:  1 } } }"
    })
    Long findAllByUserId(String userId);

    @Aggregation(pipeline = {
            "{$match: {date: {$gte: ?0, $lt: ?1}, userId: ?2}}",
            "{$group: {_id: null, count: {$sum: 1}}}",
            "{$project: {_id: 0, count: 1}}"
    })
    Long findByYearEqual(Date start, Date end, String userId);

    @Aggregation(pipeline = {
        "{$match: {userId: ?0}}",
        "{$group: {_id: '$country', count: { $sum: 1} } }",
        "{$sort: {count: -1}}",
        "{$limit: 5}"
    })
    List<StatsCountryEntity> findTop5CountryByUserId(String userId);

}
