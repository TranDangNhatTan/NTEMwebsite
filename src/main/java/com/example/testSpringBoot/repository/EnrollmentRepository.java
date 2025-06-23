package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(com.example.testSpringBoot.model.User user, com.example.testSpringBoot.model.Course course);
}