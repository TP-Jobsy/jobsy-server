package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.freelancer.FreelancerProfileAboutDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileBasicDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileContactDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.enums.Experience;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.*;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.SkillRepository;
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
import java.util.ArrayList;
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
    private SkillRepository skillRepository;

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
                .freelancerSkills(new ArrayList<>())
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
        assertNotNull(result, "Полученный dto не должен быть null");
        assertEquals(expectedDto.getId(), result.getId(), "Неверный идентификатор профиля");
        assertEquals(expectedDto.getCreatedAt(), result.getCreatedAt(), "Неверная дата создания");
        assertEquals(expectedDto.getUpdatedAt(), result.getUpdatedAt(), "Неверная дата обновления");
    }

    @Test
    void testGetProfileUserNotFound() {
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> freelancerProfileService.getProfile());
        assertTrue(exception.getMessage().contains(testEmail), "Сообщение исключения должно содержать email");
    }

    @Test
    void testUpdateBasic() {
        FreelancerProfileBasicDto basicDto = new FreelancerProfileBasicDto();
        basicDto.setCountry("Canada");
        basicDto.setCity("Toronto");
        basicDto.setFirstName("Алексей");
        basicDto.setLastName("Сергеев");
        basicDto.setPhone("+1234567890");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.setCountry(basicDto.getCountry());
        sampleProfile.setCity(basicDto.getCity());
        sampleUser.setFirstName(basicDto.getFirstName());
        sampleUser.setLastName(basicDto.getLastName());
        sampleUser.setPhone(basicDto.getPhone());
        Mockito.when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        FreelancerProfileBasicDto expectedBasic = new FreelancerProfileBasicDto();
        expectedBasic.setCountry("Canada");
        expectedBasic.setCity("Toronto");
        expectedBasic.setFirstName("Алексей");
        expectedBasic.setLastName("Сергеев");
        expectedBasic.setPhone("+1234567890");
        expectedDto.setBasic(expectedBasic);
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateBasic(basicDto);
        assertNotNull(result, "Результирующий DTO не должен быть null");
        FreelancerProfileBasicDto resultBasic = result.getBasic();
        assertNotNull(resultBasic, "Базовые данные не должны быть null");
        assertEquals("Canada", resultBasic.getCountry(), "Страна не обновлена");
        assertEquals("Toronto", resultBasic.getCity(), "Город не обновлен");
        assertEquals("Алексей", resultBasic.getFirstName(), "Имя пользователя не обновлено");
        assertEquals("Сергеев", resultBasic.getLastName(), "Фамилия пользователя не обновлена");
        assertEquals("+1234567890", resultBasic.getPhone(), "Номер телефона не обновлен");
        Mockito.verify(userRepository, Mockito.times(1)).save(sampleUser);
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
    void testUpdateAboutWithoutSkills() {
        FreelancerProfileAboutDto aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setCategoryId(20L);
        aboutDto.setSpecializationId(30L);
        aboutDto.setExperienceLevel(Experience.MIDDLE);
        aboutDto.setAboutMe("Новый опыт работы");
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.updateAbout(aboutDto);
        assertTrue(sampleProfile.getFreelancerSkills().isEmpty(), "Список навыков должен остаться пустым, если не передан");
    }

    @Test
    void testAddSkillIncrementally() {
        Skill existingSkill = Skill.builder().id(10L).name("Java").build();
        FreelancerSkill fsExisting = FreelancerSkill.builder()
                .id(new FreelancerSkillId(sampleProfile.getId(), 10L))
                .freelancerProfile(sampleProfile)
                .skill(existingSkill)
                .build();
        sampleProfile.setFreelancerSkills(new ArrayList<>(List.of(fsExisting)));
        List<Long> initialSkillIds = sampleProfile.getFreelancerSkills().stream()
                .map(fs -> fs.getSkill().getId())
                .toList();
        assertEquals(1, initialSkillIds.size(), "В профиле должен быть 1 навык");
        assertTrue(initialSkillIds.contains(10L), "Навык с id 10 должен присутствовать");
        Skill newSkill = Skill.builder().id(11L).name("Spring Boot").build();
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        Mockito.when(skillRepository.findById(11L)).thenReturn(Optional.of(newSkill));
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto resultDto = freelancerProfileService.addSkill(11L);
        List<Long> resultSkillIds = sampleProfile.getFreelancerSkills().stream()
                .map(fs -> fs.getSkill().getId())
                .toList();
        assertEquals(2, resultSkillIds.size(), "В профиле должно быть 2 навыка");
        assertTrue(resultSkillIds.contains(10L), "Навык с id 10 должен присутствовать");
        assertTrue(resultSkillIds.contains(11L), "Навык с id 11 должен быть добавлен");
    }

    @Test
    void testAddSkill() {
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.setFreelancerSkills(new ArrayList<>());
        Skill newSkill = Skill.builder().id(12L).name("Hibernate").build();
        Mockito.when(skillRepository.findById(12L)).thenReturn(Optional.of(newSkill));
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.addSkill(12L);
        List<Long> resultSkillIds = sampleProfile.getFreelancerSkills().stream()
                .map(fs -> fs.getSkill().getId())
                .toList();
        assertEquals(1, resultSkillIds.size(), "Должен быть добавлен один навык");
        assertTrue(resultSkillIds.contains(12L), "Навык с id 12 должен присутствовать");
    }

    @Test
    void testRemoveSkill() {
        Skill existingSkill = Skill.builder().id(10L).name("Java").build();
        FreelancerSkill existingFreelancerSkill = FreelancerSkill.builder()
                .id(new FreelancerSkillId(sampleProfile.getId(), 10L))
                .freelancerProfile(sampleProfile)
                .skill(existingSkill)
                .build();
        sampleProfile.setFreelancerSkills(new ArrayList<>(List.of(existingFreelancerSkill)));
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        Mockito.when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        Mockito.when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleProfile.getCreatedAt());
        expectedDto.setUpdatedAt(sampleProfile.getUpdatedAt());
        Mockito.when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        FreelancerProfileDto result = freelancerProfileService.removeSkill(10L);
        List<Long> resultSkillIds = sampleProfile.getFreelancerSkills().stream()
                .map(fs -> fs.getSkill().getId())
                .toList();
        assertEquals(0, resultSkillIds.size(), "Навык должен быть удалён, и в профиле не должно быть навыков");
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