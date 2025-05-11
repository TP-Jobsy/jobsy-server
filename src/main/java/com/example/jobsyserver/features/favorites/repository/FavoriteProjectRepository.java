package com.example.jobsyserver.features.favorites.repository;

import com.example.jobsyserver.features.favorites.model.FavoriteProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProjectRepository extends JpaRepository<FavoriteProject, Long> {
    List<FavoriteProject> findByFreelancerId(Long freelancerId);
    void deleteByFreelancerIdAndProjectId(Long freelancerId, Long projectId);

    boolean existsByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}

