package com.example.jobsyserver.features.ai.service;

import com.example.jobsyserver.features.ai.dto.GenerateDescriptionRequest;

public interface AiGenerationService {
    String generate(Long projectId, GenerateDescriptionRequest dto);
}
