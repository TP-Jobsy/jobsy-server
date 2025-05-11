package com.example.jobsyserver.service.impl.client;

import com.example.jobsyserver.features.auth.service.impl.SecurityServiceImpl;
import com.example.jobsyserver.features.client.dto.ClientProfileBasicDto;
import com.example.jobsyserver.features.client.dto.ClientProfileContactDto;
import com.example.jobsyserver.features.client.dto.ClientProfileDto;
import com.example.jobsyserver.features.client.dto.ClientProfileFieldDto;
import com.example.jobsyserver.features.client.mapper.ClientProfileMapper;
import com.example.jobsyserver.features.client.model.ClientProfile;
import com.example.jobsyserver.features.client.service.impl.ClientProfileServiceImpl;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.client.repository.ClientProfileRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientProfileServiceImplTest {

    @Mock
    private ClientProfileRepository clientProfileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientProfileMapper clientProfileMapper;
    @Mock
    private SecurityServiceImpl securityService;

    @InjectMocks
    private ClientProfileServiceImpl service;

    private final String testEmail = "test@example.com";
    private User testUser;
    private ClientProfile testProfile;
    private ClientProfileDto testProfileDto;

    @BeforeEach
    void setup(){
        testUser = User.builder().email(testEmail).build();
        testProfile = ClientProfile.builder()
                .id(1L)
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        testProfileDto = new ClientProfileDto();
    }

    @Test
    void getProfile_shouldReturnProfile() {
        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(clientProfileMapper.toDto(testProfile)).thenReturn(testProfileDto);

        ClientProfileDto result = service.getProfile();

        assertNotNull(result);
        assertEquals(testProfileDto, result);
        verify(securityService, atLeastOnce()).getCurrentUserEmail();
        verify(clientProfileMapper).toDto(testProfile);
    }

    @Test
    void updateBasic_shouldUpdateAllFields() {
        ClientProfileBasicDto dto = new ClientProfileBasicDto();
        dto.setCompanyName("New Company");
        dto.setPosition("Manager");
        dto.setCountry("Country");
        dto.setCity("City");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhone("+1234567890");

        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientProfileMapper.toDto(any(ClientProfile.class))).thenReturn(testProfileDto);

        ClientProfileDto result = service.updateBasic(dto);

        assertEquals(testProfileDto, result);
        verify(clientProfileRepository).save(testProfile);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateBasic_shouldHandlePartialUpdate() {
        ClientProfileBasicDto dto = new ClientProfileBasicDto();
        dto.setCity("New City");
        dto.setLastName("Smith");

        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientProfileMapper.toDto(any(ClientProfile.class))).thenReturn(testProfileDto);

        ClientProfileDto result = service.updateBasic(dto);

        assertEquals(testProfileDto, result);
        verify(clientProfileRepository).save(testProfile);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateContact_shouldUpdateContactLink() {
        ClientProfileContactDto dto = new ClientProfileContactDto();
        dto.setContactLink("https://new.link");

        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientProfileMapper.toDto(any(ClientProfile.class))).thenReturn(testProfileDto);

        ClientProfileDto result = service.updateContact(dto);

        assertEquals(testProfileDto, result);
        verify(clientProfileRepository).save(testProfile);
    }

    @Test
    void updateField_shouldUpdateFieldDescription() {
        ClientProfileFieldDto dto = new ClientProfileFieldDto();
        dto.setFieldDescription("New field description");

        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));
        when(clientProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        when(clientProfileRepository.save(any(ClientProfile.class))).thenReturn(testProfile);
        when(clientProfileMapper.toDto(any(ClientProfile.class))).thenReturn(testProfileDto);

        ClientProfileDto result = service.updateField(dto);

        assertEquals(testProfileDto, result);
        verify(clientProfileRepository).save(testProfile);
    }

    @Test
    void deleteAccount_shouldDeleteUser() {
        when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(testUser));

        service.deleteAccount();

        verify(userRepository).delete(testUser);
    }

    @Test
    void getAllClients_shouldReturnEmptyList() {
        when(clientProfileRepository.findAll()).thenReturn(Collections.emptyList());
        when(clientProfileMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<ClientProfileDto> result = service.getAllClients();

        assertTrue(result.isEmpty());
        verify(clientProfileRepository).findAll();
    }

    @Test
    void getClientProfileById_shouldReturnProfile() {
        Long profileId = 1L;
        when(clientProfileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));
        when(clientProfileMapper.toDto(testProfile)).thenReturn(testProfileDto);

        ClientProfileDto result = service.getClientProfileById(profileId);

        assertEquals(testProfileDto, result);
        verify(clientProfileRepository).findById(profileId);
    }
}