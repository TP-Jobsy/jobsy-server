package com.example.jobsyserver.features.project.service;

import com.example.jobsyserver.features.skill.dto.SkillDto;

import java.util.List;

public interface ProjectSkillService {
    void addSkill(Long projectId, Long skillId);
    void removeSkill(Long projectId, Long skillId);
    List<SkillDto> getSkillsByProject(Long projectId);
}
