package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.User;

public interface UserService {
    User findById(Integer id);
}