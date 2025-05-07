package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.ai.GenerateDescriptionRequest;

public interface AiGenerationService {
    String generate(Long projectId, GenerateDescriptionRequest dto);
}
