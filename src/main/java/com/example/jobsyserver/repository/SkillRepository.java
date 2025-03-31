package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}