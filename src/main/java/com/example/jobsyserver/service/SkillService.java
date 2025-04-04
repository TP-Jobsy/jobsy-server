package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SkillDto;

import java.util.List;

public interface SkillService {
    List<SkillDto> getAllSkills();
    SkillDto getSkillById(Long id);
    SkillDto createSkill(SkillDto skillDto);
    SkillDto updateSkill(Long id, SkillDto skillDto);
    void deleteSkillById(Long id);
}
