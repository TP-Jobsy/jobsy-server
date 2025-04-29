package com.example.jobsyserver.scheduler;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.service.ConfirmationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExpiredConfirmationCleanupTask {

    private final ConfirmationService confirmationService;

    @Scheduled(fixedDelayString = "${confirmation.cleanup.passwordResetInterval:600000}")
    @Transactional
    public void cleanupPasswordResetCodes() {
        var removed = confirmationService.deleteExpired(ConfirmationAction.PASSWORD_RESET);
        log.info("Password-reset cleanup: удалено {} просроченных кодов", removed.size());
    }
}
