package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
}