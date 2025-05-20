package com.example.jobsyserver.mapper;

import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapperImpl;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.common.enums.Experience;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
import com.example.jobsyserver.features.freelancer.model.FreelancerSkill;
import com.example.jobsyserver.features.freelancer.model.FreelancerSkillId;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.user.mapper.UserMapperImpl;
import com.example.jobsyserver.features.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Import({ UserMapperImpl.class, FreelancerProfileMapperImpl.class })
@SpringBootTest(classes = { UserMapperImpl.class, FreelancerProfileMapperImpl.class })
class FreelancerProfileMapperTest {

    @Autowired
    private FreelancerProfileMapper mapper;

    @Test
    void testToDto() {
        User user = User.builder()
                .id(1L)
                .email("freelancer@example.com")
                .firstName("Иван")
                .lastName("Иванов")
                .phone("+7999999999")
                .dateBirth(LocalDate.of(1990, 1, 1))
                .build();

        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 3, 30, 12, 0);
        FreelancerProfile profile = FreelancerProfile.builder()
                .id(1L)
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
                .freelancerSkills(new java.util.HashSet<>())
                .build();

        Skill skill1 = Skill.builder().id(10L).name("Java").build();
        Skill skill2 = Skill.builder().id(11L).name("Spring").build();
        FreelancerSkill fs1 = FreelancerSkill.builder()
                .id(new FreelancerSkillId(profile.getId(), skill1.getId()))
                .freelancerProfile(profile)
                .skill(skill1)
                .build();
        FreelancerSkill fs2 = FreelancerSkill.builder()
                .id(new FreelancerSkillId(profile.getId(), skill2.getId()))
                .freelancerProfile(profile)
                .skill(skill2)
                .build();

        profile.getFreelancerSkills().add(fs1);
        profile.getFreelancerSkills().add(fs2);
        FreelancerProfileDto dto = mapper.toDto(profile);
        assertNotNull(dto, "Полученный dto не должен быть null");
        assertEquals(1L, dto.getId(), "Неверный идентификатор профиля");
        assertEquals(createdAt, dto.getCreatedAt(), "Неверная дата создания");
        assertEquals(updatedAt, dto.getUpdatedAt(), "Неверная дата обновления");
        FreelancerProfileBasicDto basicDto = dto.getBasic();
        assertNotNull(basicDto, "Базовые данные не должны быть null");
        assertEquals("Россия", basicDto.getCountry(), "Неверная страна");
        assertEquals("Москва", basicDto.getCity(), "Неверный город");
        assertEquals("Иван", basicDto.getFirstName(), "Неверное имя пользователя в basic");
        assertEquals("Иванов", basicDto.getLastName(), "Неверная фамилия пользователя в basic");
        assertEquals("freelancer@example.com", basicDto.getEmail(), "Неверный email пользователя в basic");
        assertEquals("+7999999999", basicDto.getPhone(), "Неверный номер телефона в basic");
        FreelancerProfileContactDto contactDto = dto.getContact();
        assertNotNull(contactDto, "Контактные данные не должны быть null");
        assertEquals("http://portfolio.example.com", contactDto.getContactLink(), "Неверная ссылка для контакта");
        FreelancerProfileAboutDto aboutDto = dto.getAbout();
        assertNotNull(aboutDto, "Информация о профиле не должна быть null");
        assertEquals(2L, aboutDto.getCategoryId(), "Неверное значение categoryId");
        assertEquals(3L, aboutDto.getSpecializationId(), "Неверное значение specializationId");
        assertEquals(Experience.EXPERT, aboutDto.getExperienceLevel(), "Неверное значение experienceLevel");
        assertEquals("Фрилансер с опытом в разработке ПО", aboutDto.getAboutMe(), "Неверное описание о себе");
        UserDto userDto = dto.getUser();
        assertNotNull(userDto, "Данные пользователя не должны быть null");
        assertEquals("Иван", userDto.getFirstName(), "Неверное имя пользователя");
        assertEquals("Иванов", userDto.getLastName(), "Неверная фамилия пользователя");
        assertEquals("+7999999999", userDto.getPhone(), "Неверный номер телефона");
        assertEquals("freelancer@example.com", userDto.getEmail(), "Неверный email пользователя");
        List<SkillDto> skillsList = aboutDto.getSkills();
        assertNotNull(skillsList, "Список навыков не должен быть null");
        assertEquals(2, skillsList.size(), "Количество навыков не совпадает");
        List<String> skillNames = skillsList.stream()
                .map(SkillDto::getName)
                .toList();
        assertTrue(skillNames.contains("Java"), "Навык 'Java' должен присутствовать");
        assertTrue(skillNames.contains("Spring"), "Навык 'Spring' должен присутствовать");
        assertNull(dto.getAvatarUrl(), "avatarUrl должен быть null, если не установлен");
        String avatar = "http://example.com/avatar-freelancer.png";
        profile.setAvatarUrl(avatar);
        dto = mapper.toDto(profile);
        assertEquals(avatar, dto.getAvatarUrl(), "avatarUrl должен проброситься из сущности в DTO");
    }
}