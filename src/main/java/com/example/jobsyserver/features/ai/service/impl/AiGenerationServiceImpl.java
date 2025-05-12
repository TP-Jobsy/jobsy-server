package com.example.jobsyserver.features.ai.service.impl;

import com.example.jobsyserver.features.ai.service.AiGenerationService;
import com.example.jobsyserver.features.common.config.novita.NovitaProperties;
import com.example.jobsyserver.features.ai.dto.GenerateDescriptionRequest;
import com.example.jobsyserver.features.common.exception.TooManyRequestsException;
import com.example.jobsyserver.features.ai.model.AiRequest;
import com.example.jobsyserver.features.ai.model.ProjectAiHistory;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.ai.repository.AiRequestRepository;
import com.example.jobsyserver.features.ai.repository.ProjectAiHistoryRepository;
import com.example.jobsyserver.features.auth.service.SecurityService;
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
                        "stream",     false,
                        "response_format", Map.of("type","text")
                ))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        String raw = response
                .path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText("");

        String cleaned = raw.replaceAll("(?s)<think>.*?</think>\\s*", "").trim();

        req.setOutput(cleaned);
        aiRequestRepo.save(req);
        historyRepo.save(ProjectAiHistory.builder()
                .project(project)
                .aiRequest(req)
                .build());

        return cleaned;
    }

    private Bucket newBucket(Long userId) {
        Refill refill   = Refill.intervally(10, Duration.ofHours(1));
        Bandwidth limit = Bandwidth.classic(10, refill);
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}