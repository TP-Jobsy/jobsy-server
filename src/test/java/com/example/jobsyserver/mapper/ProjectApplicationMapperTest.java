package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ProjectApplicationStatus;
import com.example.jobsyserver.model.FreelancerProfile;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectApplicationMapperTest {

    @Autowired
    private ProjectApplicationMapper mapper;

    @Test
    void testToEntity_shouldMapRequestDtoToEntityWithIgnoredFields() {
        ProjectApplicationRequestDto dto = new ProjectApplicationRequestDto();
        dto.setProjectId(1L);
        dto.setFreelancerId(2L);
        dto.setStatus(ProjectApplicationStatus.PENDING);
        ProjectApplication entity = mapper.toEntity(dto);
        assertNotNull(entity);
        assertNull(entity.getId(), "ID должен быть null, так как он игнорируется");
        assertNull(entity.getCreatedAt(), "createdAt должен быть null, так как он игнорируется");
        assertNull(entity.getProject(), "project должен быть null, так как он игнорируется");
        assertNull(entity.getFreelancer(), "freelancer должен быть null, так как он игнорируется");
        assertEquals(ProjectApplicationStatus.PENDING, entity.getStatus());
    }

    @Test
    void testToDto_shouldMapEntityToDtoCorrectly() {
        Project project = new Project();
        project.setId(10L);
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(20L);
        ProjectApplication application = ProjectApplication.builder()
                .id(100L)
                .status(ProjectApplicationStatus.APPROVED)
                .project(project)
                .freelancer(freelancer)
                .createdAt(LocalDateTime.of(2024, 4, 18, 14, 0))
                .build();
        ProjectApplicationDto dto = mapper.toDto(application);
        assertNotNull(dto);
        assertEquals(100L, dto.getId());
        assertEquals(10L, dto.getProjectId());
        assertEquals(20L, dto.getFreelancerId());
        assertEquals(ProjectApplicationStatus.APPROVED, dto.getStatus());
        assertEquals(LocalDateTime.of(2024, 4, 18, 14, 0), dto.getCreatedAt());
    }
}