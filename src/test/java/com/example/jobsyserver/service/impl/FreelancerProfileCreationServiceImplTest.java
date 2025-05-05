package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.enums.UserRole;
import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class FreelancerProfileCreationServiceImplTest {

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FreelancerProfileCreationServiceImpl freelancerProfileCreationService;

    private User freelancerUser;
    private User nonFreelancerUser;
    private UserVerifiedEvent freelancerVerifiedEvent;
    private UserVerifiedEvent nonFreelancerVerifiedEvent;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        freelancerUser = User.builder()
                .id(1L)
                .email("freelancer@example.com")
                .role(UserRole.FREELANCER)
                .build();

        nonFreelancerUser = User.builder()
                .id(2L)
                .email("client@example.com")
                .role(UserRole.CLIENT)
                .build();

        freelancerVerifiedEvent = new UserVerifiedEvent(freelancerUser);
        nonFreelancerVerifiedEvent = new UserVerifiedEvent(nonFreelancerUser);
    }

    @Test
    void handleUserVerified_ShouldCreateFreelancerProfile_WhenUserIsFreelancerAndProfileDoesNotExist() {
        when(userRepository.findById(freelancerUser.getId())).thenReturn(java.util.Optional.of(freelancerUser));
        when(freelancerProfileRepository.findByUser(freelancerUser)).thenReturn(java.util.Optional.empty());
        freelancerProfileCreationService.handleUserVerified(freelancerVerifiedEvent);
        verify(freelancerProfileRepository, times(1)).save(any(FreelancerProfile.class));
    }

    @Test
    void handleUserVerified_ShouldNotCreateProfile_WhenUserIsFreelancerButProfileExists() {
        FreelancerProfile existingProfile = FreelancerProfile.builder()
                .user(freelancerUser)
                .build();
        when(userRepository.findById(freelancerUser.getId())).thenReturn(java.util.Optional.of(freelancerUser));
        when(freelancerProfileRepository.findByUser(freelancerUser)).thenReturn(java.util.Optional.of(existingProfile));
        freelancerProfileCreationService.handleUserVerified(freelancerVerifiedEvent);
        verify(freelancerProfileRepository, times(0)).save(any(FreelancerProfile.class));
    }

    @Test
    void handleUserVerified_ShouldNotCreateProfile_WhenUserIsNotFreelancer() {
        when(userRepository.findById(nonFreelancerUser.getId())).thenReturn(java.util.Optional.of(nonFreelancerUser));
        freelancerProfileCreationService.handleUserVerified(nonFreelancerVerifiedEvent);
        verify(freelancerProfileRepository, times(0)).save(any(FreelancerProfile.class));
    }

    @Test
    void handleUserVerified_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(freelancerUser.getId())).thenReturn(java.util.Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> freelancerProfileCreationService.handleUserVerified(freelancerVerifiedEvent));
    }
}
