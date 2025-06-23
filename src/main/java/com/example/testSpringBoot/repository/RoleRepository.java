package com.example.testSpringBoot.repository;

import com.example.testSpringBoot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}