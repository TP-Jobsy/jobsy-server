package com.example.jobsyserver.features.client.service.impl;

import com.example.jobsyserver.features.auth.event.UserVerifiedEvent;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.client.service.ClientProfileCreationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientProfileCreationServiceImpl implements ClientProfileCreationService {

    private final ClientProfileRepository clientProfileRepository;
    private final UserRepository userRepository;

    @Override
    @EventListener
    @Transactional
    public void handleUserVerified(UserVerifiedEvent event) { // todo: мб в один сервис вынесу
        User user = userRepository.findById(event.user().getId())
                .orElseThrow(() -> new IllegalStateException("Пользователь не найден с id: " + event.user().getId()));
        if (!user.getRole().name().equalsIgnoreCase("CLIENT")) {
            log.info("Пользователь с email {} не является заказчиком – профиль не создаётся", user.getEmail());
            return;
        }
        if (clientProfileRepository.findByUser(user).isEmpty()) {
            log.info("Создаём базовый профиль заказчика для пользователя с email: {}", user.getEmail());

            ClientProfile profile = ClientProfile.builder()
                    .user(user)
                    .companyName(null)
                    .country(null)
                    .city(null)
                    .contactLink(null)
                    .position(null)
                    .fieldDescription(null)
                    .build();
            clientProfileRepository.save(profile);
            log.info("Базовый профиль заказчика для пользователя с email {} успешно создан", user.getEmail());
        } else {
            log.info("Профиль заказчика для пользователя с email {} уже существует", user.getEmail());
        }
    }
}