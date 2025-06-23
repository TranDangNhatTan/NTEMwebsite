package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Course;
import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.repository.CourseRepository;
import com.example.testSpringBoot.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    @Override
    public void approve(Long enrollmentId) {
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOptional.isPresent()) {
            Enrollment enrollment = enrollmentOptional.get();
            enrollment.setStatus("APPROVED");
            enrollmentRepository.save(enrollment);
        }
    }

    @Override
    public void reject(Long enrollmentId) {
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOptional.isPresent()) {
            Enrollment enrollment = enrollmentOptional.get();
            enrollment.setStatus("REJECTED");
            enrollmentRepository.save(enrollment);
        }
    }

    @Override
    public void deleteById(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }

    @Override
    public List<Course> findCoursesByUserId(Integer userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserIdAndStatus(userId, "APPROVED");
        return enrollments.stream()
                .map(enrollment -> courseRepository.findById(enrollment.getCourseId()).orElse(null))
                .filter(course -> course != null)
                .collect(Collectors.toList());
    }

    // ĐÃ XÓA PHƯƠNG THỨC APPROVE(INTEGER) BỊ LỖI Ở ĐÂY
}