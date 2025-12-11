package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.SystemLog;
// import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.SystemLogRepository;
// import com.brightroute.brightroute.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemLogService {

    @Autowired
    private SystemLogRepository systemLogRepository;

    // @Autowired
    // private UserRepository userRepository;

    // تسجيل عملية جديدة
    public SystemLog logAction(String action, Long userId, String details) {
        SystemLog log = new SystemLog();
        log.setAction(action);
        log.setDetails(details);

        // if (userId != null) {
        //     User user = userRepository.findById(userId).orElse(null);
        //     log.setUser(user); // ممكن يبقى null لو الـ user مش موجود
        // }

        return systemLogRepository.save(log);
    }

    // جلب الـ logs الخاصة بـ User معين

    // public List<SystemLog> getLogsByUser(Long userId) {
    //     User user = userRepository.findById(userId)
    //             .orElseThrow(() -> new RuntimeException("User not found"));
    //     return systemLogRepository.findByUser(user);
    // }

    // جلب كل الـ logs
    public List<SystemLog> getAllLogs() {
        return systemLogRepository.findAll();
    }
}
