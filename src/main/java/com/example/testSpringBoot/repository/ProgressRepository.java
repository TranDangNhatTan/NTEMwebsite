package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
}