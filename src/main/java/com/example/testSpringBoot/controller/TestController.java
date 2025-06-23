package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test")
    public String getUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "test"; // Phải khớp với tên file test.html
    }
}