package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.*;
import com.brightroute.brightroute.repository.*;

import org.springframework.stereotype.Service;

@Service
public class EnrollmentService implements IEnrollmentService { // Implementation class

    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final UserRepository userRepository; // CORRECTION: Use UserRepository

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            LectureRepository lectureRepository,
            UserRepository userRepository // CORRECTION: Inject UserRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
        this.userRepository = userRepository; // CORRECTION: Assign UserRepository
    }

    @Override
    public Enrollment enroll(Integer lectureId, Integer userId) { // CORRECTION: Use Integer and userId

        // 1. Fetch Lecture (ID type Integer)
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found for ID: " + lectureId));

        // 2. Fetch User (CORRECTION: Fetch User, not Student)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for ID: " + userId));
        
        // OPTIONAL: Check if enrollment already exists (good practice)
        // Add a custom repository method: findByLectureIdAndUserId

        // 3. Create Enrollment (CORRECTION: Use User in constructor)
        Enrollment enrollment = new Enrollment(lecture, user);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateStatus(Integer enrollmentId, EnrollmentStatus status) { // CORRECTION: Use Integer
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found for ID: " + enrollmentId));

        enrollment.setStatus(status);

        // *** CRITICAL CORRECTION: REMOVED the following conflicting line: ***
        // if (status == EnrollmentStatus.COMPLETED) {
        //     enrollment.setComplete(true);
        // }
        // The status field itself handles completion.

        return enrollmentRepository.save(enrollment);
    }
}