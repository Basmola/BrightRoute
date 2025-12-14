package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Enrollment;
import com.brightroute.brightroute.model.EnrollmentStatus;
import com.brightroute.brightroute.dto.EnrollmentDTO;
import java.util.List;

public interface IEnrollmentService {

    Enrollment enroll(Integer lectureId, Integer userId);  

    Enrollment updateStatus(Integer enrollmentId, EnrollmentStatus status);  

    List<EnrollmentDTO> getUserEnrollments(Integer userId);
}