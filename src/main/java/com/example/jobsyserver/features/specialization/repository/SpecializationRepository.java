package com.example.jobsyserver.features.specialization.repository;

import com.example.jobsyserver.features.specialization.model.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findByCategoryId(Long categoryId);
}