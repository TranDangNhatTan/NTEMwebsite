package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.repository.EnrollmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

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
}