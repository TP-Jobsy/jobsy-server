package com.example.jobsyserver.features.auth.scheduler;

import com.example.jobsyserver.features.common.enums.ConfirmationAction;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.ConfirmationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UnverifiedUserCleanupTask {

    private final ConfirmationService confirmationService;
    private final UserRepository userRepo;

    @Scheduled(fixedDelayString = "${confirmation.cleanup.interval:3600000}")
    @Transactional
    public void cleanup() {
        var removed = confirmationService.deleteExpired(ConfirmationAction.REGISTRATION);
        removed.forEach(conf -> {
            var user = conf.getUser();
            if (!Boolean.TRUE.equals(user.getIsVerified())) {
                userRepo.delete(user);
            }
        });
        log.info("Очистка завершена: удалено {} просроченных подтверждений", removed.size());
    }
}
