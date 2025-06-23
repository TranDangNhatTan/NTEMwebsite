package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.Role;
import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.repository.RoleRepository;
import com.example.testSpringBoot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;

@Controller
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    @Transactional
    public String register(@RequestParam("username") String username,
                           @RequestParam("email") String email,
                           @RequestParam("fullName") String fullName,
                           @RequestParam("password") String password,
                           Model model) {
        if (userRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already exists!");
            return "register";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setHashedPassword(passwordEncoder.encode(password));
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        logger.debug("Saving user: {}", username);

        user = userRepository.save(user);
        logger.debug("User saved with userId: {}", user.getUserId());

        // Tạo và lưu Role
        Role role = new Role();
        role.setUser(user); // Gán User, Hibernate sẽ tự ánh xạ userId qua @MapsId
        role.setRoleType(Role.RoleType.STUDENT);
        try {
            role = roleRepository.save(role);
            logger.debug("Role saved for userId: {} with roleType: {}", role.getUserId(), role.getRoleType());
        } catch (Exception e) {
            logger.error("Failed to save Role for userId: {} - Error: {}", user.getUserId(), e.getMessage(), e);
            model.addAttribute("error", "Failed to assign role: " + e.getMessage());
            return "register";
        }

        user.setRole(role); // Cập nhật Role vào User

        model.addAttribute("message", "Registration successful! Please login.");
        return "login";
    }
}