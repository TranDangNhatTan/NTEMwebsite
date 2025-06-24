package com.example.testSpringBoot.service;

import com.example.testSpringBoot.model.LoginLog;
import com.example.testSpringBoot.repository.LoginLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class LoginLogServiceImpl implements LoginLogService {

    @Autowired
    private LoginLogRepository loginLogRepository;

    @Override
    public void log(String username, String ipAddress, boolean success) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setIpAddress(ipAddress);
        loginLog.setLoginTime(new Timestamp(System.currentTimeMillis()));
        loginLog.setStatus(success ? "SUCCESS" : "FAILURE");
        loginLogRepository.save(loginLog);
    }

    @Override
    public List<LoginLog> findAll() {
        // Sắp xếp để log mới nhất lên đầu
        return loginLogRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "loginTime"));
    }
}