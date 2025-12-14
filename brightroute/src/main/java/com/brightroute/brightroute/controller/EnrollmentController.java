package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;
import com.brightroute.brightroute.service.IEnrollmentService;
import com.brightroute.brightroute.dto.EnrollmentDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final IEnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(IEnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public Enrollment enroll(
            @RequestParam Integer lectureId,
            @RequestParam Integer userId) {
        return enrollmentService.enroll(lectureId, userId);
    }

    @PutMapping("/{id}/status")
    public Enrollment updateStatus(
            @PathVariable Integer id,
            @RequestParam EnrollmentStatus status) {
        return enrollmentService.updateStatus(id, status);
    }

    @GetMapping("/user/{userId}")
    public List<EnrollmentDTO> getUserEnrollments(@PathVariable Integer userId) {
        return enrollmentService.getUserEnrollments(userId);
    }
}