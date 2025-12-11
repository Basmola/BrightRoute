package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;
import com.brightroute.brightroute.service.IEnrollmentService; // Use the interface
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final IEnrollmentService enrollmentService; // Use the interface

    @Autowired
    public EnrollmentController(IEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public Enrollment enroll(
            @RequestParam Integer lectureId, // Consistency: Use Integer
            @RequestParam Integer userId     // CORRECTION: Use userId instead of studentId
    ) {
        return enrollmentService.enroll(lectureId, userId);
    }

    @PutMapping("/{id}/status")
    public Enrollment updateStatus(
            @PathVariable Integer id, // Consistency: Use Integer
            @RequestParam EnrollmentStatus status
    ) {
        return enrollmentService.updateStatus(id, status);
    }
}