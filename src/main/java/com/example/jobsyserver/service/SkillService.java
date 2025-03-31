package com.example.jobsyserver.service;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.mapper.SkillMapper;
import com.example.jobsyserver.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;

    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll()
                .stream()
                .map(skillMapper::toDto)
                .collect(Collectors.toList());
    }
}