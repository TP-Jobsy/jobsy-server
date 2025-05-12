package com.example.jobsyserver.features.project.service.impl;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.common.exception.BadRequestException;
import com.example.jobsyserver.features.common.exception.ResourceNotFoundException;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import com.example.jobsyserver.features.project.model.Project;
import com.example.jobsyserver.features.project.model.ProjectSkill;
import com.example.jobsyserver.features.project.model.ProjectSkillId;
import com.example.jobsyserver.features.skill.model.Skill;
import com.example.jobsyserver.features.project.repository.ProjectRepository;
import com.example.jobsyserver.features.project.repository.ProjectSkillRepository;
import com.example.jobsyserver.features.skill.repository.SkillRepository;
import com.example.jobsyserver.features.project.service.ProjectSkillService;
import org.springframework.transaction.annotation.Transactional;import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectSkillServiceImpl implements ProjectSkillService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final ProjectSkillRepository projectSkillRepository;
    private static final int MAX_SKILLS = 5;

    @Override
    @Transactional
    public void addSkill(Long projectId, Long skillId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Проект", projectId));
        long count = projectSkillRepository.countByProjectId(projectId);
        if (count >= MAX_SKILLS) {
            throw new BadRequestException("Нельзя добавить более " + MAX_SKILLS + " навыков в проект");
        }
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", skillId));
        ProjectSkillId id = new ProjectSkillId(projectId, skillId);
        if (projectSkillRepository.existsById(id)) {
            throw new BadRequestException("Навык уже добавлен в проект");
        }
        projectSkillRepository.save(new ProjectSkill(id, project, skill));
    }


    @Override
    @Transactional
    public void removeSkill(Long projectId, Long skillId) {
        ProjectSkillId id = new ProjectSkillId(projectId, skillId);
        if (!projectSkillRepository.existsById(id)) {
            throw new RuntimeException("Связь не найдена");
        }
        projectSkillRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getSkillsByProject(Long projectId) {
        return projectSkillRepository.findAllByProjectId(projectId).stream()
                .map(ps -> skillMapper.toDto(ps.getSkill()))
                .toList();
    }
}
