package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {}
