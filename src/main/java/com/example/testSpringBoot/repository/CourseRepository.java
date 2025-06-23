package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}