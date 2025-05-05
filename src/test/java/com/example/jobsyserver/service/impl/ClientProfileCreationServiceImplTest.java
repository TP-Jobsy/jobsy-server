package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.event.UserVerifiedEvent;
import com.example.jobsyserver.model.ClientProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.ClientProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.jobsyserver.enums.UserRole.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientProfileCreationServiceImplTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ClientProfileCreationServiceImpl service;

    @Test
    void handleUserVerified_shouldCreateProfileForClient() {
        User user = User.builder()
                .id(1L)
                .email("client@test.com")
                .role(CLIENT)
                .build();
        UserVerifiedEvent event = new UserVerifiedEvent(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user)).thenReturn(Optional.empty());
        service.handleUserVerified(event);
        ArgumentCaptor<ClientProfile> profileCaptor = ArgumentCaptor.forClass(ClientProfile.class);
        verify(clientProfileRepository).save(profileCaptor.capture());

        ClientProfile savedProfile = profileCaptor.getValue();
        assertEquals(user, savedProfile.getUser());
        assertNull(savedProfile.getCompanyName());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void handleUserVerified_shouldNotCreateProfileForFreelancer() {
        User user = User.builder()
                .id(2L)
                .email("freelancer@test.com")
                .role(FREELANCER)
                .build();
        UserVerifiedEvent event = new UserVerifiedEvent(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        service.handleUserVerified(event);
        verify(clientProfileRepository, never()).save(any());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void handleUserVerified_shouldNotCreateProfileIfAlreadyExists() {
        User user = User.builder()
                .id(3L)
                .email("existing@test.com")
                .role(CLIENT)
                .build();
        UserVerifiedEvent event = new UserVerifiedEvent(user);
        ClientProfile existingProfile = ClientProfile.builder().user(user).build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(clientProfileRepository.findByUser(user)).thenReturn(Optional.of(existingProfile));
        service.handleUserVerified(event);
        verify(clientProfileRepository, never()).save(any());
        verify(userRepository).findById(user.getId());
    }

    @Test
    void handleUserVerified_shouldThrowWhenUserNotFound() {
        User user = User.builder()
                .id(999L)
                .email("notfound@test.com")
                .role(CLIENT)
                .build();
        UserVerifiedEvent event = new UserVerifiedEvent(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class,
                () -> service.handleUserVerified(event));
        verify(clientProfileRepository, never()).save(any());
        verify(userRepository).findById(user.getId());
    }
}