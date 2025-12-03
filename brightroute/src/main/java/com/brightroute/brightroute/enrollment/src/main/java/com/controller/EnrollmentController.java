package enrollment.controller;

import enrollment.model.Enrollment;
import enrollment.model.EnrollmentStatus;
import enrollment.service.EnrollmentService;
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
            @RequestParam int userId
    ) {
        return enrollmentService.enroll(lectureId, userId);
    }

    @PutMapping("/{id}/status")
    public Enrollment updateStatus(
            @PathVariable int id,
            @RequestParam EnrollmentStatus status
    ) {
        return enrollmentService.updateStatus(id, status);
    }
}
