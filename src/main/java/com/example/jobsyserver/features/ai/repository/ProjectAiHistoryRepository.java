package com.example.jobsyserver.features.ai.repository;

import com.example.jobsyserver.features.ai.model.ProjectAiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAiHistoryRepository extends JpaRepository<ProjectAiHistory, Long> { }
