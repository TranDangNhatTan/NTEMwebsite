package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Course;
import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    Optional<Enrollment> findByUserAndCourse(User user, Course course);

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user u JOIN FETCH u.role JOIN FETCH e.course")
    List<Enrollment> findAllWithDetails();

    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course c WHERE e.user.userId = :userId AND e.status = :status")
    List<Enrollment> findApprovedCoursesByUserId(@Param("userId") Integer userId, @Param("status") String status);

    // THÊM PHƯƠNG THỨC MỚI NÀY
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user WHERE e.course.courseId = :courseId")
    List<Enrollment> findByCourseIdWithUserDetails(@Param("courseId") Integer courseId);
}