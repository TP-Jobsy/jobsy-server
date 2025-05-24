package com.example.jobsyserver.features.refresh.scheduler;

import com.example.jobsyserver.features.refresh.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenCleanupTask {

    private final RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void cleanupExpiredTokens() {
        Instant now = Instant.now();
        long removedCount = refreshTokenRepository
                .deleteAllByExpiryDateBeforeOrRevokedTrue(now);
        log.info("RefreshTokenCleanupTask: удалено {} просроченных/отозванных токенов", removedCount);
    }
}