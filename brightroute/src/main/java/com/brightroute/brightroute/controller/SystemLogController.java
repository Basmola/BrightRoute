package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.service.SystemLogService;

import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import jakarta.persistence.*;@RestController
@RequestMapping("/api/system-logs")
public class SystemLogController {
    @Autowired
    private SystemLogService systemLogService;

    @PostMapping
    public void log(@RequestParam String action,
                    @RequestParam Long userId,
                    @RequestParam String details) {
        systemLogService.logAction(action, userId, details);
    }

    @GetMapping
    public List<SystemLog> getAll() {
        return systemLogService.getAllLogs();
    }

    @GetMapping("/user/{userId}")
    public List<SystemLog> getByUser(@PathVariable Long userId) {
        return systemLogService.getLogsByUser(userId);
    }
}

