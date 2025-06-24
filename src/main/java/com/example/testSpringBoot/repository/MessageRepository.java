package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

// Sửa Long thành Integer để khớp với kiểu dữ liệu của messageId trong Entity
public interface MessageRepository extends JpaRepository<Message, Integer> {
}