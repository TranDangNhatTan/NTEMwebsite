package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.User;
import java.util.List; // Thêm import

public interface UserService {
    User findById(Integer id);
    List<User> findAll(); // Thêm phương thức này
}