package com.example.jobsyserver.features.rating.mapper;

import com.example.jobsyserver.features.rating.dto.RatingResponseDto;
import com.example.jobsyserver.features.rating.model.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(source = "project.id",            target = "projectId")
    @Mapping(source = "raterFreelancer.id",   target = "raterFreelancerId")
    @Mapping(source = "raterClient.id",       target = "raterClientId")
    @Mapping(source = "targetFreelancer.id",  target = "targetFreelancerId")
    @Mapping(source = "targetClient.id",      target = "targetClientId")
    RatingResponseDto toDto(Rating rating);

    List<RatingResponseDto> toDtoList(List<Rating> ratings);
}