package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByNameContainingIgnoreCase(String namePart);
}