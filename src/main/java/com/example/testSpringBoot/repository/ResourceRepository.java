package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}