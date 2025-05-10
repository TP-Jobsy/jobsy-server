package com.example.jobsyserver.dto.ai;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenerateDescriptionRequest {
    private String systemPrompt;

    @Size(min = 30, message = "Описание должно быть не менее 30 символов")
    private String userPrompt;
}
