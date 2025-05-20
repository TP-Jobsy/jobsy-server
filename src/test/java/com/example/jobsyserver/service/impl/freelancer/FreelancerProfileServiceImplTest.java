package com.example.jobsyserver.service.impl.freelancer;

import com.example.jobsyserver.features.category.dto.CategoryDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.user.model.User;
import com.example.jobsyserver.features.common.enums.Experience;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.category.service.CategoryService;
import com.example.jobsyserver.features.specialization.dto.SpecializationDto;
import com.example.jobsyserver.features.specialization.service.SpecializationService;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.repository.FreelancerProfileRepository;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.user.repository.UserRepository;
import com.example.jobsyserver.features.auth.service.SecurityService;
import com.example.jobsyserver.features.freelancer.service.impl.FreelancerProfileServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceImplTest {

    @InjectMocks
    private FreelancerProfileServiceImpl service;

    @Mock(lenient = true)
    private SecurityService securityService;
    @Mock
    private FreelancerProfileRepository repo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private SkillRepository skillRepo;
    @Mock
    private CategoryService categoryService;
    @Mock
    private SpecializationService specializationService;
    @Mock
    private FreelancerProfileMapper mapper;

    private final String email = "freelancer@example.com";
    private User user;
    private FreelancerProfile profile;
    private LocalDateTime createdAt, updatedAt;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email(email)
                .firstName("Иван")
                .lastName("Иванов")
                .dateBirth(LocalDate.of(1990, 1, 1))
                .phone("+79123456789")
                .build();
        createdAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        updatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        profile = FreelancerProfile.builder()
                .id(100L)
                .user(user)
                .experienceLevel(Experience.EXPERT)
                .categoryId(2L)
                .specializationId(3L)
                .country("Россия")
                .city("Москва")
                .aboutMe("Фрилансер с опытом в разработке ПО")
                .contactLink("http://portfolio.example.com")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .skills(new HashSet<>())
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(email, "", Collections.emptyList()),
                null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        lenient().when(securityService.getCurrentUserEmail()).thenReturn(email);
        lenient().when(securityService.getCurrentUser()).thenReturn(user);
        lenient().when(categoryService.getCategoryById(anyLong())).thenAnswer(inv -> {
            Long id = inv.getArgument(0);
            CategoryDto d = new CategoryDto();
            d.setId(id);
            d.setName("Cat" + id);
            return d;
        });
        lenient().when(specializationService.getSpecializationById(anyLong())).thenAnswer(inv -> {
            Long id = inv.getArgument(0);
            SpecializationDto d = new SpecializationDto();
            d.setId(id);
            d.setName("Spec" + id);
            return d;
        });
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetProfileSuccess() {
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        FreelancerProfileDto dtoStub = new FreelancerProfileDto();
        dtoStub.setId(profile.getId());
        dtoStub.setCreatedAt(createdAt);
        dtoStub.setUpdatedAt(updatedAt);
        var about = new FreelancerProfileAboutDto();
        about.setCategoryId(2L);
        about.setSpecializationId(3L);
        dtoStub.setAbout(about);
        when(mapper.toDto(profile)).thenReturn(dtoStub);
        CategoryDto catDto = new CategoryDto();
        catDto.setId(2L);
        catDto.setName("Веб-разработка");
        SpecializationDto spDto = new SpecializationDto();
        spDto.setId(3L);
        spDto.setName("Front-end");
        when(categoryService.getCategoryById(2L)).thenReturn(catDto);
        when(specializationService.getSpecializationById(3L)).thenReturn(spDto);
        FreelancerProfileDto res = service.getProfile();
        assertNotNull(res);
        assertEquals(100L, res.getId());
        assertEquals("Веб-разработка", res.getAbout().getCategoryName());
        assertEquals("Front-end", res.getAbout().getSpecializationName());
    }

    @Test
    void testGetProfileNotFound() {
        when(repo.findByUser(user)).thenReturn(Optional.empty());
        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getProfile()
        );
        assertTrue(ex.getMessage().contains("Профиль фрилансера"));
    }

    @Test
    void testUpdateBasic() {
        FreelancerProfileBasicDto basic = new FreelancerProfileBasicDto();
        basic.setCountry("Canada");
        basic.setCity("Toronto");
        basic.setFirstName("Алексей");
        basic.setLastName("Сергеев");
        basic.setPhone("+1234567890");
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        when(userRepo.save(user)).thenReturn(user);
        when(repo.save(profile)).thenReturn(profile);
        FreelancerProfileDto dtoStub = new FreelancerProfileDto();
        dtoStub.setBasic(basic);
        when(mapper.toDto(profile)).thenReturn(dtoStub);
        FreelancerProfileDto result = service.updateBasic(basic);
        var rb = result.getBasic();
        assertEquals("Canada", rb.getCountry());
        assertEquals("Toronto", rb.getCity());
        assertEquals("Алексей", rb.getFirstName());
        assertEquals("Сергеев", rb.getLastName());
        assertEquals("+1234567890", rb.getPhone());
    }

    @Test
    void testUpdateContact() {
        FreelancerProfileContactDto c = new FreelancerProfileContactDto();
        c.setContactLink("http://new.example.com");
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        when(repo.save(profile)).thenReturn(profile);
        when(mapper.toDto(profile)).thenReturn(new FreelancerProfileDto());
        FreelancerProfileDto res = service.updateContact(c);
        assertEquals("http://new.example.com", profile.getContactLink());
    }

    @Test
    void testUpdateAboutWithoutSkills() {
        FreelancerProfileAboutDto a = new FreelancerProfileAboutDto();
        a.setCategoryId(20L);
        a.setSpecializationId(30L);
        a.setExperienceLevel(Experience.MIDDLE);
        a.setAboutMe("Описание");
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        when(repo.save(profile)).thenReturn(profile);
        when(mapper.toDto(profile)).thenReturn(new FreelancerProfileDto());
        service.updateAbout(a);
        assertTrue(profile.getSkills().isEmpty(),
                "После updateAbout коллекция skills должна остаться пустой");
    }

    @Test
    void testAddSkillIncrementally() {
        Skill java = Skill.builder().id(10L).name("Java").build();
        profile.getSkills().add(java);
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        Skill spring = Skill.builder().id(11L).name("Spring").build();
        when(skillRepo.findById(11L)).thenReturn(Optional.of(spring));
        when(repo.save(profile)).thenReturn(profile);
        when(mapper.toDto(profile)).thenReturn(new FreelancerProfileDto());
        service.addSkill(11L);
        assertEquals(2, profile.getSkills().size());
    }

    @Test
    void testAddSkill() {
        profile.getSkills().clear();
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        Skill hib = Skill.builder().id(12L).name("Hibernate").build();
        when(skillRepo.findById(12L)).thenReturn(Optional.of(hib));
        when(repo.save(profile)).thenReturn(profile);
        when(mapper.toDto(profile)).thenReturn(new FreelancerProfileDto());
        service.addSkill(12L);
        assertEquals(1, profile.getSkills().size());
    }

    @Test
    void testRemoveSkill() {
        Skill java = Skill.builder().id(10L).name("Java").build();
        profile.getSkills().add(java);
        when(repo.findByUser(user)).thenReturn(Optional.of(profile));
        when(repo.save(profile)).thenReturn(profile);
        when(mapper.toDto(profile)).thenReturn(new FreelancerProfileDto());
        service.removeSkill(10L);
        assertTrue(profile.getSkills().isEmpty());
    }

    @Test
    void testDeleteAccount() {
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        service.deleteAccount();
        verify(userRepo).delete(user);
    }

    @Test
    void testGetAllFreelancers() {
        when(repo.findAll()).thenReturn(List.of(profile));
        FreelancerProfileDto dtoStub = new FreelancerProfileDto();
        dtoStub.setId(profile.getId());
        dtoStub.setCreatedAt(createdAt);
        dtoStub.setUpdatedAt(updatedAt);
        dtoStub.setAbout(new FreelancerProfileAboutDto());
        when(mapper.toDto(profile)).thenReturn(dtoStub);
        CategoryDto cd = new CategoryDto();
        cd.setId(profile.getCategoryId());
        cd.setName("Категория");
        SpecializationDto sd = new SpecializationDto();
        sd.setId(profile.getSpecializationId());
        sd.setName("Специализация");
        when(categoryService.getCategoryById(profile.getCategoryId())).thenReturn(cd);
        when(specializationService.getSpecializationById(profile.getSpecializationId())).thenReturn(sd);
        List<FreelancerProfileDto> out = service.getAllFreelancers();
        assertEquals(1, out.size());
        assertEquals("Категория", out.get(0).getAbout().getCategoryName());
        assertEquals("Специализация", out.get(0).getAbout().getSpecializationName());
    }

    @Test
    void testGetFreelancerProfileById() {
        Long id = profile.getId();
        when(repo.findById(id)).thenReturn(Optional.of(profile));
        FreelancerProfileDto dtoStub = new FreelancerProfileDto();
        dtoStub.setId(id);
        dtoStub.setCreatedAt(createdAt);
        dtoStub.setUpdatedAt(updatedAt);
        dtoStub.setAbout(new FreelancerProfileAboutDto());
        when(mapper.toDto(profile)).thenReturn(dtoStub);
        CategoryDto cd = new CategoryDto();
        cd.setId(profile.getCategoryId());
        cd.setName("Категория");
        SpecializationDto sd = new SpecializationDto();
        sd.setId(profile.getSpecializationId());
        sd.setName("Специализация");
        when(categoryService.getCategoryById(profile.getCategoryId())).thenReturn(cd);
        when(specializationService.getSpecializationById(profile.getSpecializationId())).thenReturn(sd);
        FreelancerProfileDto res = service.getFreelancerProfileById(id);
        assertEquals("Категория", res.getAbout().getCategoryName());
        assertEquals("Специализация", res.getAbout().getSpecializationName());
    }
}