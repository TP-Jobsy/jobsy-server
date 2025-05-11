package com.example.jobsyserver.features.skill.service;

import com.example.jobsyserver.features.skill.dto.SkillDto;

import java.util.List;

public interface SkillService {
    List<SkillDto> getAllSkills();
    SkillDto getSkillById(Long id);
    SkillDto createSkill(SkillDto skillDto);
    SkillDto updateSkill(Long id, SkillDto skillDto);
    void deleteSkillById(Long id);
    List<SkillDto> autocompleteSkills(String term);
}
