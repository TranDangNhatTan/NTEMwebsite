package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(com.example.testSpringBoot.model.User user, com.example.testSpringBoot.model.Course course);

    // Sửa truy vấn JPQL để sử dụng e.user.userId
    @Query("SELECT e FROM Enrollment e WHERE e.user.userId = :userId AND e.status = :status")
    List<Enrollment> findByUserIdAndStatus(Integer userId, String status);
}