package com.example.jobsyserver.features.rating.repository;

import com.example.jobsyserver.features.rating.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByProjectId(Long projectId);
}
