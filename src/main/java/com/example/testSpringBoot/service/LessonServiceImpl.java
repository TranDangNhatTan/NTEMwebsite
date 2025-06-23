package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Lesson;
import com.example.testSpringBoot.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        return lessonRepository.findById(id);
    }

    @Override
    public void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }

    @Override
    public void deleteById(Integer id) {
        lessonRepository.deleteById(id);
    }

    @Override
    public Optional<Integer> findMaxOrderByCourseId(Integer courseId) {
        return lessonRepository.findMaxOrderByCourseId(courseId);
    }

    @Override
    public List<Lesson> findAllWithCourse() {
        return lessonRepository.findAllWithCourse();
    }
}