package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}