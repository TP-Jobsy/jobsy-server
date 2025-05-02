package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.configuration.SkillProperties;
import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.exception.ResourceNotFoundException;
import com.example.jobsyserver.mapper.SkillMapper;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.repository.SkillRepository;
import com.example.jobsyserver.service.SkillService;
import com.example.jobsyserver.specification.SkillSpecifications;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final SkillProperties props;

    @Override
    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SkillDto getSkillById(Long id) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", id));
        return skillMapper.toDto(skill);
    }

    @Override
    public SkillDto createSkill(SkillDto skillDto) {
        Skill skill = skillMapper.toEntity(skillDto);
        Skill savedSkill = skillRepository.save(skill);
        return skillMapper.toDto(savedSkill);
    }

    @Override
    public SkillDto updateSkill(Long id, SkillDto skillDto) {
        Skill existingSkill = skillRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Навык", id));
        existingSkill.setName(skillDto.getName());
        Skill updatedSkill = skillRepository.save(existingSkill);
        return skillMapper.toDto(updatedSkill);
    }

    @Override
    public void deleteSkillById(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new ResourceNotFoundException("Навык", id);
        }
        skillRepository.deleteById(id);
    }

    @Override
    public List<SkillDto> autocompleteSkills(String term) {
        Specification<Skill> spec;
        if (term == null || term.length() < 2) {
            spec = Specification.where(SkillSpecifications.randomOrder());
        } else {
            spec = Specification.where(SkillSpecifications.nameContains(term));
        }
        var page = skillRepository.findAll(spec, PageRequest.of(0, props.popularLimit()));
        return page.getContent().stream()
                .map(skillMapper::toDto)
                .toList();
    }
}