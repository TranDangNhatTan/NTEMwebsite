package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}