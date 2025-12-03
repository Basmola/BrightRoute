package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.repository.SystemLogRepository;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import jakarta.persistence.*;

@Service
public class SystemLogService {
    @Autowired
    private SystemLogRepository systemLogRepository;

    public void logAction(String action, Long userId, String details) {
        SystemLog log = new SystemLog();
        log.setAction(action);
        log.setTimestamp(LocalDateTime.now());
        log.setUserId(userId);
        log.setDetails(details);
        systemLogRepository.save(log);
    }

    public List<SystemLog> getLogsByUser(Long userId) {
        return systemLogRepository.findByUserId(userId);
    }

    public List<SystemLog> getAllLogs() {
        return systemLogRepository.findAll();
    }
}

