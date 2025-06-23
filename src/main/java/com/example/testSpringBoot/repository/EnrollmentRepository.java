package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);

    /**
     * Tối ưu truy vấn để lấy danh sách enrollment cùng thông tin chi tiết của User và Course,
     * tránh vấn đề N+1 query trên trang quản lý.
     */
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.user JOIN FETCH e.course")
    List<Enrollment> findAllWithDetails();

    /**
     * Tối ưu truy vấn để lấy các khóa học đã đăng ký của user,
     * tránh vấn đề N+1 query trong service.
     */
    @Query("SELECT e FROM Enrollment e JOIN FETCH e.course c WHERE e.user.userId = :userId AND e.status = :status")
    List<Enrollment> findApprovedCoursesByUserId(@Param("userId") Integer userId, @Param("status") String status);
}