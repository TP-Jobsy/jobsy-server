package com.example.jobsyserver.mapper;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.model.Skill;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillDto toDto(Skill skill);
    Skill toEntity(SkillDto skillDto);
}