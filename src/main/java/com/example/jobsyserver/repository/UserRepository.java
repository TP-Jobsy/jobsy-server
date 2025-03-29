package com.example.jobsyserver.repository;

import com.example.jobsyserver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
