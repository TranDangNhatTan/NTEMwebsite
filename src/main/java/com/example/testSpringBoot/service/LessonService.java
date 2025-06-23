package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    List<Lesson> findAll();

    Optional<Lesson> findById(Integer id);

    void save(Lesson lesson);

    void deleteById(Integer id);

    Optional<Integer> findMaxOrderByCourseId(Integer courseId);

    List<Lesson> findAllWithCourse();
}