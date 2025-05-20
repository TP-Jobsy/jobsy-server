package com.example.jobsyserver.mapper;

import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapper;
import com.example.jobsyserver.features.freelancer.mapper.FreelancerProfileMapperImpl;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.skill.mapper.SkillMapperImpl;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileAboutDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileBasicDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileContactDto;
import com.example.jobsyserver.features.freelancer.dto.FreelancerProfileDto;
import com.example.jobsyserver.features.user.dto.UserDto;
import com.example.jobsyserver.features.common.enums.Experience;
import com.example.jobsyserver.features.freelancer.model.FreelancerProfile;
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

@Import({
        UserMapperImpl.class,
        SkillMapperImpl.class,
        FreelancerProfileMapperImpl.class
})
@SpringBootTest(classes = {
        UserMapperImpl.class,
        SkillMapperImpl.class,
        FreelancerProfileMapperImpl.class
})
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
                .skills(new java.util.HashSet<>())
                .build();

        Skill skill1 = Skill.builder().id(10L).name("Java").build();
        Skill skill2 = Skill.builder().id(11L).name("Spring").build();
        profile.getSkills().add(skill1);
        profile.getSkills().add(skill2);
        FreelancerProfileDto dto = mapper.toDto(profile);
        assertNotNull(dto, "Полученный dto не должен быть null");
        assertEquals(1L, dto.getId());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(updatedAt, dto.getUpdatedAt());
        FreelancerProfileBasicDto basicDto = dto.getBasic();
        assertNotNull(basicDto);
        assertEquals("Россия", basicDto.getCountry());
        assertEquals("Москва", basicDto.getCity());
        assertEquals("Иван", basicDto.getFirstName());
        assertEquals("Иванов", basicDto.getLastName());
        assertEquals("freelancer@example.com", basicDto.getEmail());
        assertEquals("+7999999999", basicDto.getPhone());
        FreelancerProfileContactDto contactDto = dto.getContact();
        assertNotNull(contactDto);
        assertEquals("http://portfolio.example.com", contactDto.getContactLink());
        FreelancerProfileAboutDto aboutDto = dto.getAbout();
        assertNotNull(aboutDto);
        assertEquals(2L, aboutDto.getCategoryId());
        assertEquals(3L, aboutDto.getSpecializationId());
        assertEquals(Experience.EXPERT, aboutDto.getExperienceLevel());
        assertEquals("Фрилансер с опытом в разработке ПО", aboutDto.getAboutMe());
        UserDto userDto = dto.getUser();
        assertNotNull(userDto);
        assertEquals("Иван", userDto.getFirstName());
        assertEquals("Иванов", userDto.getLastName());
        assertEquals("+7999999999", userDto.getPhone());
        assertEquals("freelancer@example.com", userDto.getEmail());
        List<SkillDto> skillsList = aboutDto.getSkills();
        assertNotNull(skillsList);
        assertEquals(2, skillsList.size());
        List<String> names = skillsList.stream().map(SkillDto::getName).toList();
        assertTrue(names.contains("Java"));
        assertTrue(names.contains("Spring"));
        assertNull(dto.getAvatarUrl());
        String avatar = "http://example.com/avatar-freelancer.png";
        profile.setAvatarUrl(avatar);
        dto = mapper.toDto(profile);
        assertEquals(avatar, dto.getAvatarUrl());
    }
}