package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Lesson;
import com.example.testSpringBoot.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> findById(Integer id) {
        return lessonRepository.findById(id);
    }

    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }

    public Optional<Integer> findMaxOrderByCourseId(Integer courseId) {
        return lessonRepository.findMaxOrderByCourseId(courseId);
    }

    public List<Lesson> findAllWithCourse() {
        return lessonRepository.findAllWithCourse();
    }
}