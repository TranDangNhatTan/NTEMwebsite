package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}