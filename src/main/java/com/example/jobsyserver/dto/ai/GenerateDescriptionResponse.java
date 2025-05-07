package com.example.jobsyserver.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateDescriptionResponse {
    private String generatedDescription;
}
