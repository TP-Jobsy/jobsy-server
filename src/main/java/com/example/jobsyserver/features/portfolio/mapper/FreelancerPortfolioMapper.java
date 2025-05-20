package com.example.jobsyserver.features.portfolio.mapper;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

@Primary
@Mapper(componentModel = "spring", uses = SkillMapper.class)
public interface FreelancerPortfolioMapper {

    @Mapping(target = "freelancerId", source = "freelancer.id")
    @Mapping(target = "skills", source = "skills")
    FreelancerPortfolioDto toDto(FreelancerPortfolio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FreelancerPortfolio toEntity(FreelancerPortfolioCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(FreelancerPortfolioUpdateDto dto, @MappingTarget FreelancerPortfolio entity);
}