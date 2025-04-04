package com.example.jobsyserver.service.impl;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.exception.SkillNotFoundException;
import com.example.jobsyserver.mapper.SkillMapper;
import com.example.jobsyserver.model.Skill;
import com.example.jobsyserver.repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillServiceImpl skillService;

    @Test
    void testGetAllSkills() {
        Skill skill1 = new Skill(1L, "Java");
        Skill skill2 = new Skill(2L, "Spring");
        List<Skill> skills = Arrays.asList(skill1, skill2);
        SkillDto dto1 = new SkillDto();
        dto1.setId(1L);
        dto1.setName("Java");
        SkillDto dto2 = new SkillDto();
        dto2.setId(2L);
        dto2.setName("Spring");
        when(skillRepository.findAll()).thenReturn(skills);
        when(skillMapper.toDto(skill1)).thenReturn(dto1);
        when(skillMapper.toDto(skill2)).thenReturn(dto2);
        List<SkillDto> result = skillService.getAllSkills();
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(skillRepository, times(1)).findAll();
    }

    @Test
    void testGetSkillById_found() {
        Skill skill = new Skill(1L, "Java");
        SkillDto dto = new SkillDto();
        dto.setId(1L);
        dto.setName("Java");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(skillMapper.toDto(skill)).thenReturn(dto);
        SkillDto result = skillService.getSkillById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(skillRepository, times(1)).findById(1L);
    }

    @Test
    void testGetSkillById_notFound() {
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(SkillNotFoundException.class, () -> skillService.getSkillById(1L));
        verify(skillRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateSkill() {
        SkillDto inputDto = new SkillDto();
        inputDto.setName("Python");
        Skill skillEntity = new Skill();
        skillEntity.setName("Python");
        Skill savedSkill = new Skill(1L, "Python");
        SkillDto outputDto = new SkillDto();
        outputDto.setId(1L);
        outputDto.setName("Python");
        when(skillMapper.toEntity(inputDto)).thenReturn(skillEntity);
        when(skillRepository.save(skillEntity)).thenReturn(savedSkill);
        when(skillMapper.toDto(savedSkill)).thenReturn(outputDto);
        SkillDto result = skillService.createSkill(inputDto);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Python", result.getName());
        verify(skillMapper, times(1)).toEntity(inputDto);
        verify(skillRepository, times(1)).save(skillEntity);
    }

    @Test
    void testUpdateSkill() {
        Skill existingSkill = new Skill(1L, "Python");
        SkillDto updateDto = new SkillDto();
        updateDto.setName("Advanced Python");
        Skill updatedSkill = new Skill(1L, "Advanced Python");
        SkillDto updatedDto = new SkillDto();
        updatedDto.setId(1L);
        updatedDto.setName("Advanced Python");
        when(skillRepository.findById(1L)).thenReturn(Optional.of(existingSkill));
        when(skillRepository.save(existingSkill)).thenReturn(updatedSkill);
        when(skillMapper.toDto(updatedSkill)).thenReturn(updatedDto);
        SkillDto result = skillService.updateSkill(1L, updateDto);
        assertNotNull(result);
        assertEquals("Advanced Python", result.getName());
        verify(skillRepository, times(1)).findById(1L);
        verify(skillRepository, times(1)).save(existingSkill);
    }

    @Test
    void testDeleteSkillById_found() {
        when(skillRepository.existsById(1L)).thenReturn(true);
        skillService.deleteSkillById(1L);
        verify(skillRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSkillById_notFound() {
        when(skillRepository.existsById(1L)).thenReturn(false);
        assertThrows(SkillNotFoundException.class, () -> skillService.deleteSkillById(1L));
        verify(skillRepository, times(1)).existsById(1L);
    }
}