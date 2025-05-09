package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.CategoryDto;
import com.example.jobsyserver.dto.common.SpecializationDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileAboutDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileBasicDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileContactDto;
import com.example.jobsyserver.dto.freelancer.FreelancerProfileDto;
import com.example.jobsyserver.enums.Experience;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.FreelancerSkill;
import com.example.jobsyserver.model.FreelancerSkillId;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.model.User;
import com.example.jobsyserver.repository.FreelancerProfileRepository;
import com.example.jobsyserver.repository.SkillRepository;
import com.example.jobsyserver.repository.UserRepository;
import com.example.jobsyserver.service.CategoryService;
import com.example.jobsyserver.service.SecurityService;
import com.example.jobsyserver.service.SpecializationService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @InjectMocks
    private FreelancerProfileServiceImpl freelancerProfileService;

    @Mock(lenient = true)
    private SecurityService securityService;
    @Mock
    private FreelancerProfileRepository freelancerProfileRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private SpecializationService specializationService;
    @Mock
    private FreelancerProfileMapper freelancerProfileMapper;

    private final String testEmail = "freelancer@example.com";
    private User sampleUser;
    private FreelancerProfile sampleProfile;
    private LocalDateTime sampleCreatedAt;
    private LocalDateTime sampleUpdatedAt;

    @BeforeEach
    void setup() {
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
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(testEmail, "", Collections.emptyList()),
                null,
                Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        Mockito.lenient().when(securityService.getCurrentUserEmail()).thenReturn(testEmail);
        Mockito.lenient().when(securityService.getCurrentUser()).thenReturn(sampleUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetProfileSuccess() {
        when(freelancerProfileRepository.findByUser(sampleUser))
                .thenReturn(Optional.of(sampleProfile));
        FreelancerProfileDto baseDto = new FreelancerProfileDto();
        baseDto.setId(sampleProfile.getId());
        baseDto.setCreatedAt(sampleCreatedAt);
        baseDto.setUpdatedAt(sampleUpdatedAt);
        var aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setCategoryId(2L);
        aboutDto.setSpecializationId(3L);
        baseDto.setAbout(aboutDto);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(baseDto);
        CategoryDto catDto = new CategoryDto(); catDto.setId(2L); catDto.setName("Веб-разработка");
        when(categoryService.getCategoryById(2L)).thenReturn(catDto);
        SpecializationDto specDto = new SpecializationDto(); specDto.setId(3L); specDto.setName("Front-end");
        when(specializationService.getSpecializationById(3L)).thenReturn(specDto);
        FreelancerProfileDto result = freelancerProfileService.getProfile();
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Веб-разработка", result.getAbout().getCategoryName());
        assertEquals("Front-end", result.getAbout().getSpecializationName());
    }

    @Test
    void testGetProfileNotFound() {
        when(freelancerProfileRepository.findByUser(sampleUser))
                .thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> freelancerProfileService.getProfile()
        );
        assertTrue(exception.getMessage().contains("Профиль фрилансера"),
                "Сообщение исключения должно содержать 'Профиль фрилансера'");
    }

    @Test
    void testUpdateBasic() {
        FreelancerProfileBasicDto basicDto = new FreelancerProfileBasicDto();
        basicDto.setCountry("Canada");
        basicDto.setCity("Toronto");
        basicDto.setFirstName("Алексей");
        basicDto.setLastName("Сергеев");
        basicDto.setPhone("+1234567890");
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(sampleProfile.getId());
        expectedDto.setCreatedAt(sampleCreatedAt);
        expectedDto.setUpdatedAt(sampleUpdatedAt);
        FreelancerProfileBasicDto expectedBasic = new FreelancerProfileBasicDto();
        expectedBasic.setCountry("Canada");
        expectedBasic.setCity("Toronto");
        expectedBasic.setFirstName("Алексей");
        expectedBasic.setLastName("Сергеев");
        expectedBasic.setPhone("+1234567890");
        expectedDto.setBasic(expectedBasic);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);

        FreelancerProfileDto result = freelancerProfileService.updateBasic(basicDto);

        assertNotNull(result);
        var resultBasic = result.getBasic();
        assertNotNull(resultBasic);
        assertEquals("Canada", resultBasic.getCountry());
        assertEquals("Toronto", resultBasic.getCity());
        assertEquals("Алексей", resultBasic.getFirstName());
        assertEquals("Сергеев", resultBasic.getLastName());
        assertEquals("+1234567890", resultBasic.getPhone());
        Mockito.verify(userRepository).save(sampleUser);
        Mockito.verify(freelancerProfileRepository).save(sampleProfile);
    }

    @Test
    void testUpdateContact() {
        FreelancerProfileContactDto contactDto = new FreelancerProfileContactDto();
        contactDto.setContactLink("http://newcontact.example.com");
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto result = freelancerProfileService.updateContact(contactDto);
        assertNotNull(result);
        assertEquals("http://newcontact.example.com", sampleProfile.getContactLink());
        Mockito.verify(freelancerProfileRepository).save(sampleProfile);
    }

    @Test
    void testUpdateAboutWithoutSkills() {
        FreelancerProfileAboutDto aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setCategoryId(20L);
        aboutDto.setSpecializationId(30L);
        aboutDto.setExperienceLevel(Experience.MIDDLE);
        aboutDto.setAboutMe("Новый опыт работы");
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto result = freelancerProfileService.updateAbout(aboutDto);
        assertTrue(sampleProfile.getFreelancerSkills().isEmpty());
    }

    @Test
    void testAddSkillIncrementally() {
        Skill existingSkill = Skill.builder().id(10L).name("Java").build();
        FreelancerSkill fsExisting = FreelancerSkill.builder()
                .id(new FreelancerSkillId(sampleProfile.getId(), 10L))
                .freelancerProfile(sampleProfile)
                .skill(existingSkill)
                .build();
        sampleProfile.getFreelancerSkills().add(fsExisting);
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        Skill newSkill = Skill.builder().id(11L).name("Spring Boot").build();
        when(skillRepository.findById(11L)).thenReturn(Optional.of(newSkill));
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto resultDto = freelancerProfileService.addSkill(11L);
        List<Long> ids = sampleProfile.getFreelancerSkills().stream()
                .map(fs -> fs.getSkill().getId()).toList();
        assertTrue(ids.contains(10L));
        assertTrue(ids.contains(11L));
        assertEquals(2, ids.size());
    }

    @Test
    void testAddSkill() {
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        sampleProfile.getFreelancerSkills().clear();
        Skill newSkill = Skill.builder().id(12L).name("Hibernate").build();
        when(skillRepository.findById(12L)).thenReturn(Optional.of(newSkill));
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto result = freelancerProfileService.addSkill(12L);
        assertEquals(1, sampleProfile.getFreelancerSkills().size());
    }

    @Test
    void testRemoveSkill() {
        Skill existingSkill = Skill.builder().id(10L).name("Java").build();
        FreelancerSkill fs = FreelancerSkill.builder()
                .id(new FreelancerSkillId(sampleProfile.getId(), 10L))
                .freelancerProfile(sampleProfile)
                .skill(existingSkill)
                .build();
        sampleProfile.getFreelancerSkills().add(fs);
        when(freelancerProfileRepository.findByUser(sampleUser)).thenReturn(Optional.of(sampleProfile));
        when(freelancerProfileRepository.save(sampleProfile)).thenReturn(sampleProfile);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto result = freelancerProfileService.removeSkill(10L);
        assertTrue(sampleProfile.getFreelancerSkills().isEmpty());
    }

    @Test
    void testDeleteAccount() {
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(sampleUser));
        freelancerProfileService.deleteAccount();
        Mockito.verify(userRepository).delete(sampleUser);
    }

    @Test
    void testGetAllFreelancers() {
        List<FreelancerProfile> profiles = List.of(sampleProfile);
        when(freelancerProfileRepository.findAll()).thenReturn(profiles);
        when(freelancerProfileMapper.toDtoList(profiles)).thenReturn(List.of(new FreelancerProfileDto()));
        List<FreelancerProfileDto> result = freelancerProfileService.getAllFreelancers();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testGetFreelancerProfileById() {
        Long id = sampleProfile.getId();
        when(freelancerProfileRepository.findById(id)).thenReturn(Optional.of(sampleProfile));
        FreelancerProfileDto expectedDto = new FreelancerProfileDto();
        expectedDto.setId(id);
        expectedDto.setCreatedAt(sampleCreatedAt);
        expectedDto.setUpdatedAt(sampleUpdatedAt);
        var aboutDto = new FreelancerProfileAboutDto();
        aboutDto.setCategoryId(sampleProfile.getCategoryId());
        aboutDto.setSpecializationId(sampleProfile.getSpecializationId());
        expectedDto.setAbout(aboutDto);
        when(freelancerProfileMapper.toDto(sampleProfile)).thenReturn(expectedDto);
        CategoryDto catDto = new CategoryDto();
        catDto.setId(sampleProfile.getCategoryId());
        catDto.setName("Категория");
        when(categoryService.getCategoryById(sampleProfile.getCategoryId())).thenReturn(catDto);
        SpecializationDto specDto = new SpecializationDto();
        specDto.setId(sampleProfile.getSpecializationId());
        specDto.setName("Специализация");
        when(specializationService.getSpecializationById(sampleProfile.getSpecializationId())).thenReturn(specDto);
        FreelancerProfileDto result = freelancerProfileService.getFreelancerProfileById(id);
        assertNotNull(result);
        assertEquals("Категория", result.getAbout().getCategoryName());
        assertEquals("Специализация", result.getAbout().getSpecializationName());
    }
}