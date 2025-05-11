package com.example.jobsyserver.features.portfolio.mapper;

import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioCreateDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioDto;
import com.example.jobsyserver.features.portfolio.dto.FreelancerPortfolioUpdateDto;
import com.example.jobsyserver.features.skill.dto.SkillDto;
import com.example.jobsyserver.features.portfolio.model.FreelancerPortfolio;
import com.example.jobsyserver.features.portfolio.model.PortfolioSkill;
import com.example.jobsyserver.features.skill.mapper.SkillMapper;
import org.mapstruct.*;
import org.springframework.context.annotation.Primary;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Mapper(componentModel = "spring", uses = SkillMapper.class)
public interface FreelancerPortfolioMapper {

    @Mapping(target = "freelancerId", source = "freelancer.id")
    @Mapping(target = "skills", source = "portfolioSkills", qualifiedByName = "mapPortfolioSkills")
    FreelancerPortfolioDto toDto(FreelancerPortfolio entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "portfolioSkills", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    FreelancerPortfolio toEntity(FreelancerPortfolioCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "portfolioSkills", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateFromDto(FreelancerPortfolioUpdateDto dto, @MappingTarget FreelancerPortfolio entity);

    @Named("mapPortfolioSkills")
    default List<SkillDto> mapPortfolioSkills(Set<PortfolioSkill> portfolioSkills) {
        if (portfolioSkills == null) {
            return Collections.emptyList();
        }
        return portfolioSkills.stream()
                .map(fs -> {
                    SkillDto dto = new SkillDto();
                    dto.setId(fs.getSkill().getId());
                    dto.setName(fs.getSkill().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}