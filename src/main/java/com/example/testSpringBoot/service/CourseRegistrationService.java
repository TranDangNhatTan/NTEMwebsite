package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Course;
import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.repository.CourseRepository;
import com.example.testSpringBoot.repository.EnrollmentRepository;
import com.example.testSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CourseRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(CourseRegistrationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void registerForCourse(Integer courseId) {
        SecurityContext context = SecurityContextHolder.getContext();
        executorService.submit(() -> {
            logger.info("Starting registration process for courseId: {}", courseId);
            try {
                SecurityContextHolder.setContext(context);

                User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (user == null) {
                    logger.error("No authenticated user found");
                    throw new RuntimeException("No authenticated user found");
                }
                User savedUser = userRepository.findById(user.getUserId())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                logger.info("Authenticated user: {}", savedUser.getUsername());

                Course course = courseRepository.findById(courseId)
                        .orElseThrow(() -> new RuntimeException("Course not found"));
                logger.info("Found course: {}", course.getTitle());
                if (!"active".equalsIgnoreCase(course.getStatus())) {
                    throw new RuntimeException("Course is not active");
                }

                if (enrollmentRepository.findByUserAndCourse(savedUser, course).isPresent()) {
                    throw new RuntimeException("Already registered for this course");
                }

                Enrollment enrollment = new Enrollment();
                enrollment.setUser(savedUser);
                enrollment.setCourse(course);
                enrollment.setStatus("pending");
                enrollment.setEnrolledAt(new Timestamp(System.currentTimeMillis()));
                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
                logger.info("Enrollment saved with id: {}, userId: {}, courseId: {}", savedEnrollment.getEnrollmentId(), savedUser.getUserId(), courseId);

            } catch (Exception e) {
                logger.error("Registration failed for courseId {}: {}", courseId, e.getMessage());
                throw new RuntimeException("Registration failed: " + e.getMessage());
            } finally {
                SecurityContextHolder.clearContext();
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}