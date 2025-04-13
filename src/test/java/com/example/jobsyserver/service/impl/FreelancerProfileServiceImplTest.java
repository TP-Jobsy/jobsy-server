package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.*;
import com.example.jobsyserver.enums.Experience;
import com.example.jobsyserver.exception.UserNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.SecurityService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;

    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private FreelancerProfileMapper freelancerProfileMapper;

    private final String testEmail = "freelancer@example.com";
    private User sampleUser;
    private FreelancerProfile sampleProfile;
    private LocalDateTime sampleCreatedAt;
    private LocalDateTime sampleUpdatedAt;

    @BeforeEach
    void setup() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(testEmail, "", Collections.emptyList()),
                null,
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.lenient().when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        sampleUser = User.builder()
                .id(1L)
                .email(testEmail)
                .firstName("Иван")
                .lastName("Иванов")
                .dateBirth(LocalDate.of(1990, 1, 1))
                .phone("+79123456789")
                .build();

        sampleCreatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        sampleUpdatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);

        sampleProfile = FreelancerProfile.builder()
                .id(100L)
                .user(sampleUser)
                .experienceLevel(Experience.EXPERT)
                .categoryId(2L)
                .specializationId(3L)
                .country("Россия")
                .city("Москва")
                .aboutMe("Фрилансер с опытом в разработке ПО")
                .contactLink("http://portfolio.example.com")
                .createdAt(sampleCreatedAt)
                .updatedAt(sampleUpdatedAt)
                .build();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetProfileSuccess() {
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleCreatedAt);
        expectedDto.setUpdatedAt(sampleUpdatedAt);
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.getProfile();
        assertNotNull(result, "Полученный дто не должен быть null");
        assertEquals(expectedDto.getId(), result.getId(), "Неверный идентификатор профиля");
        assertEquals(expectedDto.getCreatedAt(), result.getCreatedAt(), "Неверная дата создания");
        assertEquals(expectedDto.getUpdatedAt(), result.getUpdatedAt(), "Неверная дата обновления");
    }

    @Test
    void testGetProfileUserNotFound() {
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> freelancerProfileService.getProfile());
        assertTrue(exception.getMessage().contains(testEmail), "Сообщение исключения должно содержать email");
    }

    @Test
    void testUpdateBasic() {
        FreelancerProfileBasicDto basicDto = new FreelancerProfileBasicDto();
        basicDto.setCountry("Canada");
        basicDto.setCity("Toronto");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.setCountry(basicDto.getCountry());
        sampleProfile.setCity(basicDto.getCity());
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateBasic(basicDto);
        assertNotNull(result, "Результирующий dto не должен быть null");
        assertEquals("Canada", sampleProfile.getCountry(), "Страна не обновлена");
        assertEquals("Toronto", sampleProfile.getCity(), "Город не обновлен");
        Mockito.verify(freelancerProfileRepository, Mockito.times(1)).save(sampleProfile);
    }

    @Test
    void testUpdateContact() {
        FreelancerProfileContactDto contactDto = new FreelancerProfileContactDto();
        contactDto.setContactLink("http://newcontact.example.com");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.setContactLink(contactDto.getContactLink());
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateContact(contactDto);
        assertNotNull(result, "Результирующий dto не должен быть null");
        assertEquals("http://newcontact.example.com", sampleProfile.getContactLink(), "Контактная ссылка не обновлена");
        Mockito.verify(freelancerProfileRepository, Mockito.times(1)).save(sampleProfile);
    }

    @Test
    void testUpdateAbout() {
        FreelancerProfileAboutDto aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setCategoryId(10L);
        aboutDto.setSpecializationId(20L);
        aboutDto.setExperienceLevel(Experience.MIDDLE);
        aboutDto.setAboutMe("Обновить информацию о себе");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.setCategoryId(aboutDto.getCategoryId());
        sampleProfile.setSpecializationId(aboutDto.getSpecializationId());
        sampleProfile.setExperienceLevel(aboutDto.getExperienceLevel());
        sampleProfile.setAboutMe(aboutDto.getAboutMe());
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateAbout(aboutDto);
        assertNotNull(result, "Результирующий dto не должен быть null");
        assertEquals(10L, sampleProfile.getCategoryId(), "CategoryId не обновлен");
        assertEquals(20L, sampleProfile.getSpecializationId(), "SpecializationId не обновлен");
        assertEquals(aboutDto.getExperienceLevel(), sampleProfile.getExperienceLevel(), "Уровень опыта не обновлен");
        assertEquals("Обновить информацию о себе", sampleProfile.getAboutMe(), "Описание 'о себе' не обновлено");
        Mockito.verify(freelancerProfileRepository, Mockito.times(1)).save(sampleProfile);
    }

    @Test
    void testUpdateUser() {
        FreelancerProfileUserDto userDto = new FreelancerProfileUserDto();
        userDto.setFirstName("Анастасия");
        userDto.setLastName("Смирнова");
        userDto.setPhone("+799999999999");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleUser.setFirstName(userDto.getFirstName());
        sampleUser.setLastName(userDto.getLastName());
        sampleUser.setPhone(userDto.getPhone());
        Mockito.when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateUser(userDto);
        assertNotNull(result, "Результирующий dto не должен быть null");
        assertEquals("Анастасия", sampleUser.getFirstName(), "Имя пользователя не обновлено");
        assertEquals("Смирнова", sampleUser.getLastName(), "Фамилия пользователя не обновлена");
        assertEquals("+799999999999", sampleUser.getPhone(), "Телефон пользователя не обновлен");
        Mockito.verify(userRepository, Mockito.times(1)).save(sampleUser);
    }

    @Test
    void testDeleteAccount() {
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        freelancerProfileService.deleteAccount();
        Mockito.verify(userRepository, Mockito.times(1)).delete(sampleUser);
    }

    @Test
    void testGetAllFreelancers() {
        List<FreelancerProfile> profiles = List.of(sampleProfile);
        Mockito.when(freelancerProfileRepository.findAll()).thenReturn(profiles);
        List<FreelancerProfileDto> expectedDtoList = List.of(new FreelancerProfileDto());
        Mockito.when(freelancerProfileMapper.toDtoList(profiles)).thenReturn(expectedDtoList);
        List<FreelancerProfileDto> result = freelancerProfileService.getAllFreelancers();
        assertNotNull(result, "Результирующий список не должен быть null");
        assertFalse(result.isEmpty(), "Список профилей не должен быть пустым");
        assertEquals(expectedDtoList.size(), result.size(), "Размер списка не совпадает");
    }

    @Test
    void testGetFreelancerProfileById() {
        Long profileId = sampleProfile.getId();
        Mockito.when(freelancerProfileRepository.findById(profileId)).thenReturn(Optional.of(sampleProfile));
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(profileId);
        expectedDto.setCreatedAt(sampleCreatedAt);
        expectedDto.setUpdatedAt(sampleUpdatedAt);
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.getFreelancerProfileById(profileId);
        assertNotNull(result, "Результирующий dto не должен быть null");
        assertEquals(profileId, result.getId(), "Идентификатор профиля не совпадает");
    }
}