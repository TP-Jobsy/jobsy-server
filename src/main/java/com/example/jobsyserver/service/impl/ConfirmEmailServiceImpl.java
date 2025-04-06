package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.ConfirmationAction;
import com.example.jobsyserver.model.Confirmation;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ConfirmationRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.ConfirmEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConfirmEmailServiceImpl implements ConfirmEmailService {

    private final ConfirmationRepository confirmationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void confirmEmail(String email, String confirmationCode) {
        Confirmation confirmation = confirmationRepository
                .findByUserEmailAndActionAndUsedFalse(email, ConfirmationAction.REGISTRATION)
                .orElseThrow(() -> new RuntimeException("Запись подтверждения не найдена или уже использована"));
        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            User user = confirmation.getUser();
            confirmationRepository.delete(confirmation);
            userRepository.delete(user);
            throw new RuntimeException("Срок подтверждения истёк. Пожалуйста, зарегистрируйтесь снова");
        }

        if (!confirmation.getConfirmationCode().equals(confirmationCode)) {
            throw new RuntimeException("Неверный код подтверждения");
        }
        confirmation.setUsed(true);
        confirmationRepository.save(confirmation);
        User user = confirmation.getUser();
        user.setIsVerified(true);
        userRepository.save(user);
    }
}