package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.CourseSubscription;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.CourseSubscriptionRepository;
import com.brightroute.brightroute.repository.UserRepository;

import com.brightroute.brightroute.dto.CourseSubscriptionDTO; // 1. NEW: Import the DTO

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseSubscriptionService {

    private final CourseSubscriptionRepository courseSubscriptionRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public CourseSubscriptionService(
            CourseSubscriptionRepository courseSubscriptionRepository,
            UserRepository userRepository,
            CourseRepository courseRepository
    ) {
        this.courseSubscriptionRepository = courseSubscriptionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    // 2. MODIFIED: Return type is now CourseSubscriptionDTO
    public CourseSubscriptionDTO subscribe(Integer userId, Integer courseId) {
        
        // Check if subscription already exists
        if (courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) {
            throw new RuntimeException("User is already subscribed to this course");
        }

        // Fetch User and Course entities
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Create and save the entity
        CourseSubscription subscription = new CourseSubscription(user, course);
        CourseSubscription savedSubscription = courseSubscriptionRepository.save(subscription);

        // 3. NEW: Convert the saved JPA entity to the DTO before returning
        return new CourseSubscriptionDTO(savedSubscription); 
    }

    @Transactional
    public void unsubscribe(Integer userId, Integer courseId) {
        if (!courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) {
            throw new RuntimeException("Subscription not found");
        }

        courseSubscriptionRepository.deleteByUserIdAndCourseCourseId(userId, courseId);
    }
}