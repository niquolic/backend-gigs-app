package com.example.backendgigsapp.mapper;

import com.example.backendgigsapp.dto.GigRequest;
import com.example.backendgigsapp.dto.GigResponse;
import com.example.backendgigsapp.entity.GigsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GigMapper {

    @Mapping(target = "userId", ignore = true)
    GigsEntity toEntity(GigRequest request);

    GigResponse toResponse(GigsEntity entity);

    List<GigResponse> toResponses(List<GigsEntity> entities);
}
