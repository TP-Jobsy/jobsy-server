package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.configuration.NovitaProperties;
import com.example.jobsyserver.dto.ai.GenerateDescriptionRequest;
import com.example.jobsyserver.exception.TooManyRequestsException;
import com.example.jobsyserver.model.AiRequest;
import com.example.jobsyserver.model.ProjectAiHistory;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.AiRequestRepository;
import com.example.jobsyserver.repository.ProjectAiHistoryRepository;
import com.example.jobsyserver.service.AiGenerationService;
import com.example.jobsyserver.service.SecurityService;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerationService {

    private final WebClient novitaWebClient;
    private final AiRequestRepository aiRequestRepo;
    private final ProjectAiHistoryRepository historyRepo;
    private final SecurityService security;
    private final NovitaProperties props;

    private final ConcurrentMap<Long, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public String generate(Long projectId, GenerateDescriptionRequest dto) {
        User user    = security.getCurrentUser();
        var  project = security.getProjectReference(projectId);

        Bucket bucket = buckets.computeIfAbsent(user.getId(), this::newBucket);
        if (!bucket.tryConsume(1)) {
            throw new TooManyRequestsException("Превышен лимит AI-запросов (макс 10 в час)");
        }

        AiRequest req = AiRequest.builder()
                .user(user)
                .project(project)
                .systemPrompt(dto.getSystemPrompt())
                .userPrompt(dto.getUserPrompt())
                .build();
        aiRequestRepo.save(req);

        List<Map<String,String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role",    "system",
                "content", props.defaultSystemPrompt()
        ));
        if (dto.getSystemPrompt() != null && !dto.getSystemPrompt().isBlank()) {
            messages.add(Map.of(
                    "role",    "system",
                    "content", dto.getSystemPrompt()
            ));
        }
        messages.add(Map.of(
                "role",    "user",
                "content", dto.getUserPrompt()
        ));

        JsonNode response = novitaWebClient.post()
                .uri("/chat/completions")
                .bodyValue(Map.of(
                        "model",           props.model(),
                        "messages",        messages,
                        "response_format", Map.of("type","text")
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        JsonNode firstChoice = response.path("choices").path(0);
        String output = firstChoice
                .path("message")
                .path("content")
                .asText("");

        req.setOutput(output);
        aiRequestRepo.save(req);
        historyRepo.save(ProjectAiHistory.builder()
                .project(project)
                .aiRequest(req)
                .build());

        return output;
    }

    private Bucket newBucket(Long userId) {
        Refill refill   = Refill.intervally(10, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}