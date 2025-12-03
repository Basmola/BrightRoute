package enrollment.service.impl;

import enrollment.model.Enrollment;
import enrollment.model.EnrollmentStatus;
import enrollment.repository.EnrollmentRepository;
import enrollment.service.EnrollmentService;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public Enrollment enroll(int lectureId, int userId) {
        Enrollment enrollment = new Enrollment(lectureId, userId);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateStatus(int enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(status);

        if (status == EnrollmentStatus.COMPLETED)
            enrollment.setComplete(true);

        return enrollmentRepository.save(enrollment);
    }
}
