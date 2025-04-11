package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.FreelancerProfileCreationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreelancerProfileCreationServiceImpl implements FreelancerProfileCreationService {

    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;

    @Override
    @EventListener
    @Transactional
    public void handleUserVerified(UserVerifiedEvent event) {
        User user = userRepository.findById(event.user().getId())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с id: " + event.user().getId()));
        if (!user.getRole().name().equalsIgnoreCase("FREELANCER")) {
            log.info("Пользователь с email {} не является фрилансером – профиль не создаётся", user.getEmail());
            return;
        }
        if (freelancerProfileRepository.findByUser(user).isEmpty()) {
            FreelancerProfile profile = FreelancerProfile.builder()
                    .user(user)
                    .experienceLevel(null)
                    .aboutMe("")
                    .contactLink("")
                    .categoryId(null)
                    .specializationId(null)
                    .country(null)
                    .city(null)
                    .build();
            freelancerProfileRepository.save(profile);
        }
    }
}
