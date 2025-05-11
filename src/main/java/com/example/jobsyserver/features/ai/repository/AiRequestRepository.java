package com.example.jobsyserver.features.ai.repository;

import com.example.jobsyserver.features.ai.model.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {}
