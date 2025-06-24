package com.example.testSpringBoot.config.handlers;

import com.example.testSpringBoot.service.LoginLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private LoginLogService loginLogService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        String ipAddress = request.getRemoteAddr();
        loginLogService.log(username, ipAddress, false);

        setDefaultFailureUrl("/login?error"); // Chuyển hướng về trang login với param error
        super.onAuthenticationFailure(request, response, exception);
    }
}