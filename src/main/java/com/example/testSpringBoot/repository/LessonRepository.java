package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    @Query("SELECT MAX(l.lessonOrder) FROM Lesson l WHERE l.courseId = :courseId")
    Optional<Integer> findMaxOrderByCourseId(Integer courseId);

    @Query("SELECT l FROM Lesson l JOIN FETCH l.course")
    List<Lesson> findAllWithCourse();
}