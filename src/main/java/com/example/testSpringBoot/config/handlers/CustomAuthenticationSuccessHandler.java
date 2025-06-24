package com.example.testSpringBoot.config.handlers;

import com.example.testSpringBoot.service.LoginLogService;
import jakarta.servlet.ServletException; // Thêm import này
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LoginLogService loginLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException { // <-- THÊM ServletException VÀO ĐÂY
        String username = authentication.getName();
        String ipAddress = request.getRemoteAddr();
        loginLogService.log(username, ipAddress, true);

        setDefaultTargetUrl("/index"); // Chuyển hướng về trang chủ sau khi thành công
        super.onAuthenticationSuccess(request, response, authentication);
    }
}