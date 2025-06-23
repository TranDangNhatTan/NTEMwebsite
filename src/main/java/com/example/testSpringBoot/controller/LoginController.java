package com.example.testSpringBoot.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URLDecoder;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Model model, HttpServletRequest request) {
        model.addAttribute("currentPage", request.getRequestURI());
        String message = request.getParameter("message");
        if (message != null) {
            try {
                model.addAttribute("message", URLDecoder.decode(message, "UTF-8"));
            } catch (Exception e) {
                // Xử lý fallback nếu decode thất bại, bao gồm cả chuỗi đơn giản
                if ("Registration_successful_Please_login".equals(message)) {
                    model.addAttribute("message", "Đăng ký thành công! Vui lòng đăng nhập.");
                } else {
                    model.addAttribute("message", "Please login.");
                }
            }
        }
        return "login"; // Trả về trang login.html
    }
}