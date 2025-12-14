package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.*;
import com.brightroute.brightroute.repository.*;
import com.brightroute.brightroute.dto.EnrollmentDTO;

import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class EnrollmentService implements IEnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            LectureRepository lectureRepository,
            UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Enrollment enroll(Integer lectureId, Integer userId) {

        // 1. Fetch Lecture
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + lectureId));

        // 2. Fetch User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));

        // 3. Create Enrollment
        Enrollment enrollment = new Enrollment(lecture, user);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateStatus(Integer enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found for ID: " + enrollmentId));

        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public java.util.List<EnrollmentDTO> getUserEnrollments(Integer userId) {
        java.util.List<Enrollment> enrollments = enrollmentRepository.findByUser_Id(userId);
        return enrollments.stream().map(e -> new EnrollmentDTO(
                e.getEnrollmentId(),
                e.getLecture().getId(),
                e.getUser().getId(),
                e.getStatus(),
                e.getDateEnrolled())).collect(Collectors.toList());
    }
}