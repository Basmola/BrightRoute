package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.SystemLogRepository;
import com.brightroute.brightroute.repository.UserRepository;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemLogService {

    private final SystemLogRepository systemLogRepository;
    private final UserRepository userRepository;  

    @Autowired
    public SystemLogService(SystemLogRepository systemLogRepository, UserRepository userRepository) {
        this.systemLogRepository = systemLogRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public SystemLog logAction(String action, Integer userId, String details) {  
        SystemLog log = new SystemLog();
        log.setAction(action);
        log.setDetails(details);

        if (userId != null) {
             
            User user = userRepository.findById(userId).orElse(null);
             
            log.setUser(user); 
        }

        return systemLogRepository.save(log);
    }

    public List<SystemLog> getLogsByUser(Integer userId) {  

        return systemLogRepository.findByUser_IdOrderByTimestampDesc(userId);
    }

    public List<SystemLog> getAllLogs() {
        return systemLogRepository.findAll();
    }
}