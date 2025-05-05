package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.exception.BadRequestException;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.SkillMapper;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectSkill;
import com.example.jobsyserver.model.ProjectSkillId;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.ProjectSkillRepository;
import com.example.jobsyserver.repository.SkillRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectSkillServiceImplTest {

    @Mock(lenient = true)
    private ProjectRepository projectRepository;
    @Mock(lenient = true)
    private SkillRepository skillRepository;
    @Mock(lenient = true)
    private SkillMapper skillMapper;
    @Mock(lenient = true)
    private ProjectSkillRepository projectSkillRepository;

    @InjectMocks
    private ProjectSkillServiceImpl projectSkillService;

    private Project project;
    private Skill skill;
    private SkillDto skillDto;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setId(1L);
        skill = new Skill();
        skill.setId(1L);
        skill.setName("Java");

        skillDto = new SkillDto();
        skillDto.setId(1L);
        skillDto.setName("Java");
    }

    @Test
    void addSkill_ShouldAddSkill_WhenValidData() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(projectSkillRepository.countByProjectId(1L)).thenReturn(2L);
        when(projectSkillRepository.existsById(any(ProjectSkillId.class))).thenReturn(false);
        projectSkillService.addSkill(1L, 1L);
        verify(projectSkillRepository, times(1)).save(any(ProjectSkill.class));
    }

    @Test
    void addSkill_ShouldThrowBadRequestException_WhenMaxSkillsExceeded() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(projectSkillRepository.countByProjectId(1L)).thenReturn(5L);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> projectSkillService.addSkill(1L, 1L));
        assertEquals("Нельзя добавить более 5 навыков в проект", exception.getMessage());
    }

    @Test
    void addSkill_ShouldThrowResourceNotFoundException_WhenProjectNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> projectSkillService.addSkill(1L, 1L));
        assertEquals("Проект с идентификатором '1' не найден", exception.getMessage());
    }


    @Test
    void addSkill_ShouldThrowResourceNotFoundException_WhenSkillNotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> projectSkillService.addSkill(1L, 1L));
        assertEquals("Навык с идентификатором '1' не найден", exception.getMessage());
    }


    @Test
    void addSkill_ShouldThrowBadRequestException_WhenSkillAlreadyAdded() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(projectSkillRepository.countByProjectId(1L)).thenReturn(2L);
        when(projectSkillRepository.existsById(any(ProjectSkillId.class))).thenReturn(true);
        BadRequestException exception = assertThrows(BadRequestException.class, () -> projectSkillService.addSkill(1L, 1L));
        assertEquals("Навык уже добавлен в проект", exception.getMessage());
    }

    @Test
    void removeSkill_ShouldRemoveSkill_WhenSkillExists() {
        when(projectSkillRepository.existsById(any(ProjectSkillId.class))).thenReturn(true);
        projectSkillService.removeSkill(1L, 1L);
        verify(projectSkillRepository, times(1)).deleteById(any(ProjectSkillId.class));
    }

    @Test
    void removeSkill_ShouldThrowRuntimeException_WhenSkillLinkNotFound() {
        when(projectSkillRepository.existsById(any(ProjectSkillId.class))).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> projectSkillService.removeSkill(1L, 1L));
        assertEquals("Связь не найдена", exception.getMessage());
    }

    @Test
    void getSkillsByProject_ShouldReturnSkills_WhenValidProjectId() {
        List<ProjectSkill> projectSkills = List.of(new ProjectSkill(new ProjectSkillId(1L, 1L), project, skill));
        when(projectSkillRepository.findAllByProjectId(1L)).thenReturn(projectSkills);
        when(skillMapper.toDto(skill)).thenReturn(skillDto);
        List<SkillDto> result = projectSkillService.getSkillsByProject(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
    }

    @Test
    void getSkillsByProject_ShouldReturnEmptyList_WhenNoSkills() {
        when(projectSkillRepository.findAllByProjectId(1L)).thenReturn(Collections.emptyList());
        List<SkillDto> result = projectSkillService.getSkillsByProject(1L);
        assertTrue(result.isEmpty());
    }

}