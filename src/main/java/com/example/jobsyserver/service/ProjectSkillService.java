package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SkillDto;

import java.util.List;

public interface ProjectSkillService {
    void addSkill(Long projectId, Long skillId);
    void removeSkill(Long projectId, Long skillId);
    List<SkillDto> getSkillsByProject(Long projectId);
}
