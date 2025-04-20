package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.project.ProjectApplicationDto;
import com.example.jobsyserver.dto.project.ProjectApplicationRequestDto;
import com.example.jobsyserver.enums.ApplicationType;
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
        assertNull(entity.getApplicationType(), "type должен быть null, так как он игнорируется");
        assertEquals(ProjectApplicationStatus.PENDING, entity.getStatus(), "status должен быть взят из DTO");
    }

    @Test
    void testToDto_shouldMapEntityToDtoCorrectly() {
        Project project = new Project();
        project.setId(10L);
        FreelancerProfile freelancer = new FreelancerProfile();
        freelancer.setId(20L);
        LocalDateTime now = LocalDateTime.of(2025, 4, 20, 12, 0);
        ProjectApplication application = ProjectApplication.builder()
                .id(100L)
                .project(project)
                .freelancer(freelancer)
                .status(ProjectApplicationStatus.APPROVED)
                .applicationType(ApplicationType.RESPONSE)
                .createdAt(now)
                .build();
        ProjectApplicationDto dto = mapper.toDto(application);
        assertNotNull(dto);
        assertEquals(100L, dto.getId(), "ID маппер должен скопировать из сущности");
        assertEquals(10L, dto.getProjectId(), "projectId должен скопироваться из entity.project.id");
        assertEquals(20L, dto.getFreelancerId(), "freelancerId должен скопироваться из entity.freelancer.id");
        assertEquals(ProjectApplicationStatus.APPROVED, dto.getStatus(), "status должен скопироваться");
        assertEquals(ApplicationType.RESPONSE, dto.getApplicationType(), "type должен скопироваться");
        assertEquals(now, dto.getCreatedAt(), "createdAt должен скопироваться");
    }
}