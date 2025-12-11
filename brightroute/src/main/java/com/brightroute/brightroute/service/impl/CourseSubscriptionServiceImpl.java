package com.brightroute.brightroute.service.impl;

import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.CourseSubscription;
import com.brightroute.brightroute.model.Student;
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.CourseSubscriptionRepository;
import com.brightroute.brightroute.repository.StudentRepository;
import com.brightroute.brightroute.service.CourseSubscriptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseSubscriptionServiceImpl implements CourseSubscriptionService {

    private final CourseSubscriptionRepository courseSubscriptionRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public CourseSubscriptionServiceImpl(
            CourseSubscriptionRepository courseSubscriptionRepository,
            StudentRepository studentRepository,
            CourseRepository courseRepository
    ) {
        this.courseSubscriptionRepository = courseSubscriptionRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseSubscription subscribe(Long studentId, Long courseId) {
        // Check if subscription already exists
        if (courseSubscriptionRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Student is already subscribed to this course");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseSubscription subscription = new CourseSubscription(student, course);

        return courseSubscriptionRepository.save(subscription);
    }

    @Override
    @Transactional
    public void unsubscribe(Long studentId, Long courseId) {
        if (!courseSubscriptionRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("Subscription not found");
        }

        courseSubscriptionRepository.deleteByStudentIdAndCourseId(studentId, courseId);
    }
}
