package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Enrollment;

import java.util.List;

public interface EnrollmentService {
    List<Enrollment> findAll();
    void approve(Long enrollmentId);
    void reject(Long enrollmentId);
    void deleteById(Long enrollmentId);
}