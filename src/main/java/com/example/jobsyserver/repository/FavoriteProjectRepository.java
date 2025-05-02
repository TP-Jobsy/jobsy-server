package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.FavoriteProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteProjectRepository extends JpaRepository<FavoriteProject, Long> {
    List<FavoriteProject> findByFreelancerId(Long freelancerId);
    void deleteByFreelancerIdAndProjectId(Long freelancerId, Long projectId);
}

