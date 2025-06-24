package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    private UserService userService; // Đảm bảo bạn có service này và interface của nó

    @GetMapping("/api/users")
    public List<UserDTO> findUsers(@RequestParam(value = "search", required = false, defaultValue = "") String searchTerm) {
        List<User> users = userService.findAll(); // Cần thêm phương thức này vào UserService
        return users.stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getFullName(),
                        user.getRole() != null ? user.getRole().getRoleType().name() : "N/A"
                ))
                .filter(userDTO -> userDTO.getFullName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class UserDTO {
        private Integer id;
        private String fullName;
        private String role;
    }
}