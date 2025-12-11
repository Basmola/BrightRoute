package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;

public interface EnrollmentService {

    Enrollment enroll(int lectureId, int studentId);

    Enrollment updateStatus(int enrollmentId, EnrollmentStatus status);
}
