package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.SystemLogRepository;
import com.brightroute.brightroute.repository.UserRepository; // REQUIRED
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SystemLogService {

    private final SystemLogRepository systemLogRepository;
    private final UserRepository userRepository; // REQUIRED

    @Autowired
    public SystemLogService(SystemLogRepository systemLogRepository, UserRepository userRepository) {
        this.systemLogRepository = systemLogRepository;
        this.userRepository = userRepository;
    }

    // تسجيل عملية جديدة
    @Transactional
    public SystemLog logAction(String action, Integer userId, String details) { // CORRECTION: Integer userId
        SystemLog log = new SystemLog();
        log.setAction(action);
        log.setDetails(details);

        if (userId != null) {
            // Fetch the User entity to set the relationship
            User user = userRepository.findById(userId).orElse(null);
            // This will set the user FK (user_id) in the SystemLog table
            log.setUser(user); 
        }

        return systemLogRepository.save(log);
    }

    // جلب الـ logs الخاصة بـ User معين
    public List<SystemLog> getLogsByUser(Integer userId) { // CORRECTION: Integer userId
        // Use the custom repository method to find logs by the user's ID
        // Note: No need to fetch the User entity first; repository handles it.
        return systemLogRepository.findByUser_IdOrderByTimestampDesc(userId);
    }

    // جلب كل الـ logs
    public List<SystemLog> getAllLogs() {
        return systemLogRepository.findAll();
    }
}