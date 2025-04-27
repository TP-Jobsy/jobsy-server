package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.configuration.ConfirmationProperties;
import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import com.example.jobsyserver.service.ConfirmationService;
import com.example.jobsyserver.util.ConfirmationCodeGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class ConfirmationServiceImpl implements ConfirmationService {
    private final ConfirmationRepository repo;
    private final ConfirmationProperties props;

    @Override
    public Confirmation createConfirmationFor(User user, ConfirmationAction action) {
        repo.findByUserEmailAndActionAndUsedFalse(user.getEmail(), action)
                .ifPresent(repo::delete);
        var code = ConfirmationCodeGenerator.generateNumericCode(props.codeLength());
        var conf = Confirmation.builder()
                .user(user)
                .action(action)
                .confirmationCode(code)
                .expiresAt(LocalDateTime.now().plusMinutes(props.expirationMinutes()))
                .used(false)
                .build();
        return repo.save(conf);
    }

    @Override
    public Confirmation validateAndUse(String email, String code, ConfirmationAction action) {
        var conf = repo.findByUserEmailAndActionAndUsedFalse(email, action)
                .orElseThrow(() -> new BadRequestException("Код не найден или уже использован"));
        if (conf.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Срок действия кода истёк");
        }
        if (!conf.getConfirmationCode().equals(code)) {
            throw new BadRequestException("Неверный код подтверждения");
        }
        conf.setUsed(true);
        return repo.save(conf);
    }

    @Override
    @Transactional
    public List<Confirmation> deleteExpired(ConfirmationAction action) {
        var now = LocalDateTime.now();
        var expired = repo.findAllByActionAndUsedFalseAndExpiresAtBefore(action, now);
        repo.deleteAll(expired);
        return expired;
    }

    @Override
    public Optional<Confirmation> findActiveByEmail(String email, ConfirmationAction action) {
        return repo.findByUserEmailAndActionAndUsedFalse(email, action)
                .filter(c -> c.getExpiresAt().isAfter(LocalDateTime.now()));
    }
}

