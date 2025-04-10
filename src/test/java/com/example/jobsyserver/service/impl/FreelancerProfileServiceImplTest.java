package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileUpdateDto;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FreelancerProfileServiceImplTest {

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FreelancerProfileMapper freelancerProfileMapper;

    private User testUser;
    private FreelancerProfile testProfile;
    private final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .email(TEST_EMAIL)
                .firstName("Test")
                .lastName("User")
                .phone("1234567890")
                .dateBirth(LocalDate.of(2000, 1, 1))
                .isActive(true)
                .isVerified(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testProfile = FreelancerProfile.builder()
                .id(1L)
                .user(testUser)
                .experienceLevel(null)
                .categoryId(2L)
                .specializationId(3L)
                .aboutMe("About test")
                .contactLink("http://contact.test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TestingAuthenticationToken authToken = new TestingAuthenticationToken(TEST_EMAIL, "password", "ROLE_FREELANCER");
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetProfileSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(freelancerProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        when(freelancerProfileMapper.toDto(testProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.getProfile();
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(freelancerProfileRepository, times(1)).findByUser(testUser);
    }

    @Test
    void testGetProfileUserNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            freelancerProfileService.getProfile();
        });
        assertTrue(exception.getMessage().contains("Пользователь не найден"));
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
    }

    @Test
    void testGetProfileProfileNotFound() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(freelancerProfileRepository.findByUser(testUser)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            freelancerProfileService.getProfile();
        });
        assertTrue(exception.getMessage().contains("Профиль фрилансера не найден"));
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(freelancerProfileRepository, times(1)).findByUser(testUser);
    }

    @Test
    void testUpdateProfileSuccess() {
        FreelancerProfileUpdateDto updateDto = new FreelancerProfileUpdateDto();
         updateDto.setFirstName("NewFirstName");
         updateDto.setLastName("NewLastName");
         updateDto.setPhone("0987654321");
        updateDto.setContactLink("http://new-contact.test");
        updateDto.setExperienceLevel("Middle");
        updateDto.setAboutMe("Updated about me");
        updateDto.setCategoryId(5L);
        updateDto.setSpecializationId(6L);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        when(freelancerProfileRepository.findByUser(testUser)).thenReturn(Optional.of(testProfile));
        doNothing().when(freelancerProfileMapper).updateEntityFromDto(updateDto, testProfile);
        when(freelancerProfileRepository.save(testProfile)).thenReturn(testProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        when(freelancerProfileMapper.toDto(testProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateProfile(updateDto);
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(freelancerProfileRepository, times(1)).findByUser(testUser);
        verify(freelancerProfileMapper, times(1)).updateEntityFromDto(updateDto, testProfile);
        verify(freelancerProfileRepository, times(1)).save(testProfile);
    }

    @Test
    void testDeleteAccountSuccess() {
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));
        freelancerProfileService.deleteAccount();
        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    void testGetCurrentUserEmailNotAuthenticated() {
        SecurityContextHolder.clearContext();
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> {
            freelancerProfileService.getProfile();
        });
        assertTrue(exception.getMessage().contains("Пользователь не аутентифицирован"));
    }

    @Test
    void testGetAllFreelancersSuccess() {
        List<FreelancerProfile> profiles = List.of(testProfile);
        when(freelancerProfileRepository.findAll()).thenReturn(profiles);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        when(freelancerProfileMapper.toDto(testProfile)).thenReturn(expectedDto);
        when(freelancerProfileMapper.toDtoList(profiles)).thenReturn(List.of(expectedDto));
        List<FreelancerProfileDto> result = freelancerProfileService.getAllFreelancers();
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Ожидался непустой список профилей");
        assertEquals(1, result.size());
        assertEquals(expectedDto, result.get(0));
        verify(freelancerProfileRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFreelancersEmpty() {
        when(freelancerProfileRepository.findAll()).thenReturn(Collections.emptyList());
        List<FreelancerProfileDto> result = freelancerProfileService.getAllFreelancers();
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(freelancerProfileRepository, times(1)).findAll();
    }

    @Test
    void testGetFreelancerProfileByIdSuccess() {
        Long profileId = 1L;
        when(freelancerProfileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        when(freelancerProfileMapper.toDto(testProfile)).thenReturn(expectedDto);

        FreelancerProfileDto result = freelancerProfileService.getFreelancerProfileById(profileId);
        assertNotNull(result);
        assertEquals(expectedDto, result);
        verify(freelancerProfileRepository, times(1)).findById(profileId);
    }

    @Test
    void testGetFreelancerProfileByIdNotFound() {
        Long profileId = 100L;
        when(freelancerProfileRepository.findById(profileId)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            freelancerProfileService.getFreelancerProfileById(profileId);
        });
        assertTrue(exception.getMessage().contains("Фрилансер не найден"));
        verify(freelancerProfileRepository, times(1)).findById(profileId);
    }
}