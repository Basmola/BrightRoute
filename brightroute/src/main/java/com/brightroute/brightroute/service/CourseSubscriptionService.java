package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.CourseSubscription;
import com.brightroute.brightroute.model.User; // CORRECTION: Use User
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.CourseSubscriptionRepository;
import com.brightroute.brightroute.repository.UserRepository; // CORRECTION: Use UserRepository

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseSubscriptionService { // Assuming this is the implementation class

    private final CourseSubscriptionRepository courseSubscriptionRepository;
    private final UserRepository userRepository; // CORRECTION: Changed StudentRepository to UserRepository
    private final CourseRepository courseRepository;

    public CourseSubscriptionService(
            CourseSubscriptionRepository courseSubscriptionRepository,
            UserRepository userRepository, // CORRECTION: Using UserRepository
            CourseRepository courseRepository
    ) {
        this.courseSubscriptionRepository = courseSubscriptionRepository;
        this.userRepository = userRepository; // CORRECTION: Assigned to userRepository
        this.courseRepository = courseRepository;
    }

    public CourseSubscription subscribe(Integer userId, Integer courseId) { // CORRECTION: Changed Long to Integer
        // Check if subscription already exists
        if (courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) { // CORRECTION: Logic updated
            throw new RuntimeException("User is already subscribed to this course");
        }

        User user = userRepository.findById(userId) // CORRECTION: Fetch User
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseSubscription subscription = new CourseSubscription(user, course); // CORRECTION: Use User

        return courseSubscriptionRepository.save(subscription);
    }

    @Transactional
    public void unsubscribe(Integer userId, Integer courseId) { // CORRECTION: Changed Long to Integer
        if (!courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) { // CORRECTION: Logic updated
            throw new RuntimeException("Subscription not found");
        }

        courseSubscriptionRepository.deleteByUserIdAndCourseCourseId(userId, courseId); // CORRECTION: Logic updated
    }
}