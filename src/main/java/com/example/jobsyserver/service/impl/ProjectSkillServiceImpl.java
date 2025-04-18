package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.exception.ProjectNotFoundException;
import com.example.jobsyserver.mapper.SkillMapper;
import com.example.jobsyserver.model.Project;
import com.example.jobsyserver.model.ProjectSkill;
import com.example.jobsyserver.model.ProjectSkillId;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.repository.ProjectRepository;
import com.example.jobsyserver.repository.ProjectSkillRepository;
import com.example.jobsyserver.repository.SkillRepository;
import com.example.jobsyserver.service.ProjectSkillService;
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

    @Override
    @Transactional
    public void addSkill(Long projectId, Long skillId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Проект не найден"));
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Навык не найден"));

        ProjectSkillId id = new ProjectSkillId(projectId, skillId);
        if (projectSkillRepository.existsById(id)) {
            throw new RuntimeException("Навык уже добавлен");
        }
        projectSkillRepository.save(
                ProjectSkill.builder().id(id).project(project).skill(skill).build()
        );
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
