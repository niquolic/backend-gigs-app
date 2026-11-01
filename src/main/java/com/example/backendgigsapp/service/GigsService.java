package com.example.backendgigsapp.service;

import com.example.backendgigsapp.dto.GigRequest;
import com.example.backendgigsapp.dto.GigResponse;
import com.example.backendgigsapp.entity.GigsEntity;
import com.example.backendgigsapp.mapper.GigMapper;
import com.example.backendgigsapp.repository.GigsRepository;
import org.springframework.data.domain.Sort;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class GigsService {

    private final GigsRepository gigsRepo;
    private final GigMapper gigMapper;

    public GigsService(GigsRepository gigsRepo, GigMapper gigMapper) {
        this.gigsRepo = gigsRepo;
        this.gigMapper = gigMapper;
    }

    public List<GigResponse> getGigsByUserId(String userId) {
        return gigMapper.toResponses(
                gigsRepo.findByUserIdOrderByDateDesc(userId, Sort.by(Sort.Order.desc("date")))
        );
    }

    public GigResponse getGigById(String id, String userId) {
        return gigMapper.toResponse(getOwnedGig(id, userId));
    }

    @CacheEvict(cacheNames = {"bandStats", "gigStats", "countryStats"}, allEntries = true)
    public GigResponse addGig(GigRequest request, String userId) {
        GigsEntity gig = gigMapper.toEntity(request);
        // Never allow a client-provided ID to overwrite an existing document.
        gig.setId(null);
        gig.setUserId(userId);
        return gigMapper.toResponse(gigsRepo.save(gig));
    }

    @CacheEvict(cacheNames = {"bandStats", "gigStats", "countryStats"}, allEntries = true)
    public void deleteGig(String id, String userId) {
        gigsRepo.delete(getOwnedGig(id, userId));
    }

    @CacheEvict(cacheNames = {"bandStats", "gigStats", "countryStats"}, allEntries = true)
    public GigResponse editGig(GigRequest request, String userId) {
        if (request.id() == null || request.id().isBlank()) {
            throw new IllegalArgumentException("A gig ID is required.");
        }

        GigsEntity existing = getOwnedGig(request.id(), userId);
        GigsEntity updated = gigMapper.toEntity(request);
        // Preserve the ID and owner from the persisted document, never from the request.
        updated.setId(existing.getId());
        updated.setUserId(existing.getUserId());
        return gigMapper.toResponse(gigsRepo.save(updated));
    }

    private GigsEntity getOwnedGig(String id, String userId) {
        GigsEntity gig = gigsRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Gig not found."));
        if (gig.getUserId() == null || !gig.getUserId().equals(userId)) {
            // Do not disclose whether another user's document exists.
            throw new NoSuchElementException("Gig not found.");
        }
        return gig;
    }
}
