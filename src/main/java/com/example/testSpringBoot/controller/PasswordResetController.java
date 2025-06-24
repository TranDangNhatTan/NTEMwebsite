package com.example.testSpringBoot.controller;

import com.example.testSpringBoot.model.PasswordResetToken;
import com.example.testSpringBoot.model.User;
import com.example.testSpringBoot.repository.PasswordResetTokenRepository;
import com.example.testSpringBoot.repository.UserRepository;
import com.example.testSpringBoot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordResetTokenRepository tokenRepository;
    @Autowired private EmailService emailService;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String userEmail, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài khoản nào với email này.");
            return "redirect:/forgot-password";
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        tokenRepository.save(myToken);

        String subject = "NTEM English - Yêu cầu đặt lại mật khẩu";
        String body = "<h1>Yêu cầu đặt lại mật khẩu</h1>"
                + "<p>Chào " + user.getFullName() + ",</p>"
                + "<p>Bạn đã yêu cầu đặt lại mật khẩu. Mã xác thực của bạn là:</p>"
                + "<h2 style='color:blue;'>" + token + "</h2>"
                + "<p>Mã này sẽ hết hạn trong 24 giờ.</p>"
                + "<p>Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>";

        try {
            emailService.sendHtmlEmail(user.getEmail(), subject, body);
            redirectAttributes.addFlashAttribute("message", "Một mã xác thực đã được gửi đến email của bạn.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi gửi email. Vui lòng thử lại.");
        }

        return "redirect:/reset-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(value = "token", required = false) String token, Model model) {
        if (token != null) {
            PasswordResetToken resetToken = tokenRepository.findByToken(token);
            if (resetToken == null || resetToken.isExpired()) {
                model.addAttribute("error", "Mã xác thực không hợp lệ hoặc đã hết hạn.");
                return "reset-password-form";
            }
            model.addAttribute("token", token);
        }
        return "reset-password-form";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("password") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword,
                                       RedirectAttributes redirectAttributes) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        if (resetToken == null || resetToken.isExpired()) {
            redirectAttributes.addFlashAttribute("error", "Mã xác thực không hợp lệ hoặc đã hết hạn.");
            return "redirect:/reset-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu không khớp.");
            return "redirect:/reset-password?token=" + token;
        }

        User user = resetToken.getUser();
        user.setHashedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // Xóa token sau khi đã sử dụng

        redirectAttributes.addFlashAttribute("message", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
        return "redirect:/login";
    }
}