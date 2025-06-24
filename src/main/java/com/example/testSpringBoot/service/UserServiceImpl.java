package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List; // Thêm import

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override // Thêm triển khai cho phương thức mới
    public List<User> findAll() {
        return userRepository.findAll();
    }
}