package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.ProjectSkill;
import com.example.jobsyserver.model.ProjectSkillId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectSkillRepository extends JpaRepository<ProjectSkill, ProjectSkillId> {
    List<ProjectSkill> findAllByProjectId(Long projectId);
    long countByProjectId(Long projectId);
}