package com.example.jobsyserver.contoller;

import com.example.jobsyserver.dto.common.SkillDto;
import com.example.jobsyserver.service.SkillService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
@Tag(name = "skills", description = "Получение списка навыков")
public class SkillController {

    private final SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillDto>> getAll() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }
}
