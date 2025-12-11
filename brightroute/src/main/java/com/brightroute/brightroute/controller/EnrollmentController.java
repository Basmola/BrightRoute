package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;
import com.brightroute.brightroute.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollment")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll")
    public Enrollment enroll(
            @RequestParam int lectureId,
            @RequestParam int studentId
    ) {
        return enrollmentService.enroll(lectureId, studentId);
    }

    @PutMapping("/{id}/status")
    public Enrollment updateStatus(
            @PathVariable int id,
            @RequestParam EnrollmentStatus status
    ) {
        return enrollmentService.updateStatus(id, status);
    }
}
