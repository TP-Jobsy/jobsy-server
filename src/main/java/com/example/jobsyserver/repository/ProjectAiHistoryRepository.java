package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.ProjectAiHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectAiHistoryRepository extends JpaRepository<ProjectAiHistory, Long> { }
