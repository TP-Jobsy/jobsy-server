package com.example.jobsyserver.features.skill.mapper;

import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.skill.model.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillDto toDto(Skill skill);
    Skill toEntity(SkillDto skillDto);
}