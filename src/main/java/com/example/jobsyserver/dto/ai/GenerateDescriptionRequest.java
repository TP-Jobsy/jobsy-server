package com.example.jobsyserver.dto.ai;

import lombok.Data;

@Data
public class GenerateDescriptionRequest {
    private String systemPrompt;
    private String userPrompt;
}
