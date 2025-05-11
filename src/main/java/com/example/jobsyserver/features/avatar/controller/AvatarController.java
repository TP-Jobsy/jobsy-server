package com.example.jobsyserver.features.avatar.controller;

import com.example.jobsyserver.features.common.exception.AvatarStorageException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.avatar.service.AvatarStorageService;
import com.example.jobsyserver.features.auth.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/profile")
@ConditionalOnProperty(prefix="s3", name="enabled", havingValue="true")
@RequiredArgsConstructor
public class AvatarController {
    private final AvatarStorageService storage;
    private final SecurityService security;
    private final ClientProfileRepository clients;
    private final FreelancerProfileRepository freelancers;

    @Operation(summary = "Загрузить аватар заказчика")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар загружен успешно"),
            @ApiResponse(responseCode = "404", description = "Профиль клиента не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сохранения аватара")
    })
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/client/avatar")
    public ResponseEntity<String> uploadClientAvatar(@RequestParam MultipartFile file) {
        String email = security.getCurrentUserEmail();
        log.debug("Загрузка аватара заказчика: текущий email = {}", email);

        ClientProfile profile = clients.findByUserEmail(email)
                .orElseThrow(() -> {
                    log.error("Профиль клиента не найден для email={}", email);
                    return new ResourceNotFoundException("Клиент", "email", email);
                });
        log.debug("Найден профиль клиента, id={}", profile.getId());

        try {
            log.debug("Старт загрузки аватара для клиента id={}", profile.getId());
            String url = storage.uploadAvatar("client", profile.getId(), file);
            log.info("Аватар загружен, url={}", url);

            profile.setAvatarUrl(url);
            clients.save(profile);
            log.debug("Сохранён avatarUrl в БД для клиента id={}", profile.getId());

            return ResponseEntity.ok(url);

        } catch (Exception e) {
            log.error("Ошибка загрузки аватара для clientId={}", profile.getId(), e);
            throw new AvatarStorageException("Не удалось сохранить аватар: " + e.getMessage());
        }
    }

    @Operation(summary = "Загрузить аватар фрилансера")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Аватар загружен успешно"),
            @ApiResponse(responseCode = "404", description = "Профиль фрилансера не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка сохранения аватара")
    })
    @PreAuthorize("hasRole('FREELANCER')")
    @PostMapping("/freelancer/avatar")
    public ResponseEntity<String> uploadFreelancerAvatar(@RequestParam MultipartFile file) {
        String email = security.getCurrentUserEmail();
        log.debug("Загрузка аватара фрилансера: текущий email = {}", email);
        FreelancerProfile profile = freelancers.findByUserEmail(email)
                .orElseThrow(() -> {
                    log.error("Профиль фрилансера не найден для email={}", email);
                    return new ResourceNotFoundException("Фрилансер", "email", email);
                });
        log.debug("Найден профиль фрилансера, id={}", profile.getId());
        try {
            log.debug("Старт загрузки аватара для фрилансера id={}", profile.getId());
            String url = storage.uploadAvatar("freelancer", profile.getId(), file);
            log.info("Аватар загружен, url={}", url);
            profile.setAvatarUrl(url);
            freelancers.save(profile);
            log.debug("Сохранён avatarUrl в БД для фрилансера id={}", profile.getId());
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("Ошибка загрузки аватара для freelancerId={}", profile.getId(), e);
            throw new AvatarStorageException("Не удалось сохранить аватар: " + e.getMessage());
        }
    }
}