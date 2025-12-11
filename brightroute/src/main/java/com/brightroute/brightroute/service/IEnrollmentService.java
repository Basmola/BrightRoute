package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;

public interface IEnrollmentService {

    Enrollment enroll(Integer lectureId, Integer userId); // CORRECTION: Use Integer and userId

    Enrollment updateStatus(Integer enrollmentId, EnrollmentStatus status); // CORRECTION: Use Integer
}