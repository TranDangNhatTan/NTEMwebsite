package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.LoginLog;
import java.util.List;

public interface LoginLogService {
    void log(String username, String ipAddress, boolean success);
    List<LoginLog> findAll();
}