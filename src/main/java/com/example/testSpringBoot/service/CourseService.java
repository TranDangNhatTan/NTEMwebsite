package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Course;
import java.util.List;

public interface CourseService {
    List<Course> findAll();
    void save(Course course);
    void deleteById(Integer id);
}