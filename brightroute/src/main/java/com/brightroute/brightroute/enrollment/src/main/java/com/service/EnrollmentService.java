package enrollment.service;

import enrollment.model.Enrollment;
import enrollment.model.EnrollmentStatus;

public interface EnrollmentService {

    Enrollment enroll(int lectureId, int userId);

    Enrollment updateStatus(int enrollmentId, EnrollmentStatus status);
}
