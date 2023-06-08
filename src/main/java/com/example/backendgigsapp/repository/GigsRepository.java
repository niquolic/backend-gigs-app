package com.example.backendgigsapp.repository;
import com.example.backendgigsapp.entities.GigsEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GigsRepository extends CrudRepository<GigsEntity, Long> {

    List<GigsEntity> findByUserIdOrderByDateDesc(Long id);

    Optional<GigsEntity> findById(Long id);

}
