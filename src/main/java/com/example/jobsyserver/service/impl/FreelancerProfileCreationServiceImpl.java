package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.service.FreelancerProfileCreationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FreelancerProfileCreationServiceImpl implements FreelancerProfileCreationService {

    private final FreelancerProfileRepository freelancerProfileRepository;

    @Override
    @EventListener
    @Transactional
    public void handleUserVerified(UserVerifiedEvent event) {
        User user = event.user();
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
