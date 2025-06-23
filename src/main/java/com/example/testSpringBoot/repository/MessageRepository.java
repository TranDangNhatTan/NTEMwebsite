package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}