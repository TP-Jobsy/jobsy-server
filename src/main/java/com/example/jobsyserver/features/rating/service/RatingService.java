package com.example.jobsyserver.features.rating.service;

import com.example.jobsyserver.features.rating.dto.RatingResponseDto;

import java.util.List;

public interface RatingService {
    void rateProject(Long projectId, Long raterProfileId, Integer score);
    List<RatingResponseDto> getRatings(Long projectId);
}
