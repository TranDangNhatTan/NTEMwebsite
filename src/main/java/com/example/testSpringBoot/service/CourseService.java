package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();
    Optional<Course> findById(Integer id);
    void save(Course course);
    void deleteById(Integer id);
}