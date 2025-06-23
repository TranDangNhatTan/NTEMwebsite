package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}