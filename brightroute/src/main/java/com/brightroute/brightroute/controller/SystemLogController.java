package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.SystemLog;
import com.brightroute.brightroute.service.SystemLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-logs")
public class SystemLogController {

    @Autowired
    private SystemLogService systemLogService;

    @PostMapping
    public ResponseEntity<SystemLog> log(@RequestParam String action,
            @RequestParam(required = false) Integer userId,  
            @RequestParam(required = false) String details) {
        SystemLog log = systemLogService.logAction(action, userId, details);
        return ResponseEntity.ok(log);
    }

    @GetMapping
    public ResponseEntity<List<SystemLog>> getAll() {
        List<SystemLog> logs = systemLogService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SystemLog>> getByUser(@PathVariable Integer userId) {  
        List<SystemLog> logs = systemLogService.getLogsByUser(userId);
        return ResponseEntity.ok(logs);
    }
}