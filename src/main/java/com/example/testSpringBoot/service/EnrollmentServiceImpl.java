package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.Course;
import com.example.testSpringBoot.model.Enrollment;
import com.example.testSpringBoot.repository.EnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    // Thêm Logger để theo dõi
    private static final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findAll() {
        return enrollmentRepository.findAllWithDetails();
    }

    @Override
    @Transactional
    public void approve(Long enrollmentId) {
        logger.info("BẮT ĐẦU: Đang cố gắng phê duyệt enrollment ID: {}", enrollmentId);
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOptional.isPresent()) {
            Enrollment enrollment = enrollmentOptional.get();
            logger.info("Đã tìm thấy enrollment. Trạng thái hiện tại: {}", enrollment.getStatus());

            enrollment.setStatus("APPROVED");

            // THAY ĐỔI QUAN TRỌNG: Sử dụng saveAndFlush để ép ghi xuống DB ngay lập tức
            enrollmentRepository.saveAndFlush(enrollment);

            logger.info("THÀNH CÔNG: Đã đổi trạng thái thành APPROVED và flush cho enrollment ID: {}", enrollmentId);
        } else {
            logger.warn("CẢNH BÁO: Không tìm thấy enrollment với ID: {}", enrollmentId);
            throw new RuntimeException("Enrollment not found with id: " + enrollmentId);
        }
    }

    @Override
    @Transactional
    public void reject(Long enrollmentId) {
        Optional<Enrollment> enrollmentOptional = enrollmentRepository.findById(enrollmentId);
        if (enrollmentOptional.isPresent()) {
            Enrollment enrollment = enrollmentOptional.get();
            enrollment.setStatus("REJECTED");
            enrollmentRepository.saveAndFlush(enrollment); // Dùng saveAndFlush cho nhất quán
        }
    }

    @Override
    @Transactional
    public void deleteById(Long enrollmentId) {
        enrollmentRepository.deleteById(enrollmentId);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Course> findCoursesByUserId(Integer userId) {
        List<Enrollment> enrollments = enrollmentRepository.findApprovedCoursesByUserId(userId, "APPROVED");
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }
}