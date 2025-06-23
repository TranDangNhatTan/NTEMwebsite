package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}