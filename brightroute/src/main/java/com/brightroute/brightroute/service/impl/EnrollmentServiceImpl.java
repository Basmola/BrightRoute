package com.brightroute.brightroute.service.impl;

import com.brightroute.brightroute.model.*;
import com.brightroute.brightroute.repository.*;
import com.brightroute.brightroute.service.EnrollmentService;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    public EnrollmentServiceImpl(
            EnrollmentRepository enrollmentRepository,
            LectureRepository lectureRepository,
            StudentRepository studentRepository
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.lectureRepository = lectureRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Enrollment enroll(int lectureId, int studentId) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("Lecture not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Enrollment enrollment = new Enrollment(lecture, student);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment updateStatus(int enrollmentId, EnrollmentStatus status) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(status);

        if (status == EnrollmentStatus.COMPLETED) {
            enrollment.setComplete(true);
        }

        return enrollmentRepository.save(enrollment);
    }
}
