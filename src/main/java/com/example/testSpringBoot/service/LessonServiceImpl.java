package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Lesson;
import com.example.testSpringBoot.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl extends LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
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
    public int findMaxOrderByCourseId(Integer courseId) {
        return lessonRepository.findMaxOrderByCourseId(courseId)
                .orElse(0); // Trả về 0 nếu không tìm thấy giá trị max
    }

    @Override
    public Optional<Lesson> findById(Integer id) {
        return lessonRepository.findById(id); // Gọi trực tiếp từ repository
    }
}