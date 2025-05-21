package com.example.jobsyserver.features.project.service.impl;

import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.project.service.ProjectSkillService;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectSkillServiceImpl implements ProjectSkillService {
    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    @Override
    public List<SkillDto> getSkills(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        return project.getSkills()
                .stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addSkill(Long projectId, Long skillId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", skillId));
        project.getSkills().add(skill);
        projectRepository.save(project);
    }

    @Override
    @Transactional
    public void removeSkill(Long projectId, Long skillId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        boolean removed = project.getSkills().removeIf(s -> s.getId().equals(skillId));
        if (!removed) {
            throw new ResourceNotFoundException("Навык " + skillId + " не найден в проекте " + projectId);
        }
        projectRepository.save(project);
    }
}