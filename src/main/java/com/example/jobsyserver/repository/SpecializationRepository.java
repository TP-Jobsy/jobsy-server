package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findByCategoryId(Long categoryId);
}