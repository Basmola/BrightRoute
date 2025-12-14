package com.brightroute.brightroute.service;

import com.brightroute.brightroute.model.Course;
import com.brightroute.brightroute.model.CourseSubscription;
import com.brightroute.brightroute.model.User;
import com.brightroute.brightroute.repository.CourseRepository;
import com.brightroute.brightroute.repository.CourseSubscriptionRepository;
import com.brightroute.brightroute.repository.UserRepository;

import com.brightroute.brightroute.dto.CourseSubscriptionDTO;
import com.brightroute.brightroute.dto.CourseDTO;

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
            CourseRepository courseRepository) {
        this.courseSubscriptionRepository = courseSubscriptionRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public CourseSubscriptionDTO subscribe(Integer userId, Integer courseId) {

        if (courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) {
            throw new RuntimeException("User is already subscribed to this course");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseSubscription subscription = new CourseSubscription(user, course);
        CourseSubscription savedSubscription = courseSubscriptionRepository.save(subscription);

        return new CourseSubscriptionDTO(savedSubscription);
    }

    @Transactional
    public void unsubscribe(Integer userId, Integer courseId) {
        if (!courseSubscriptionRepository.existsByUserIdAndCourseCourseId(userId, courseId)) {
            throw new RuntimeException("Subscription not found");
        }

        courseSubscriptionRepository.deleteByUserIdAndCourseCourseId(userId, courseId);
    }

    public java.util.List<CourseDTO> getSubscribedCourses(Integer userId) {
        return courseSubscriptionRepository.findByUserId(userId)
                .stream()
                .map(subscription -> {
                    Course course = subscription.getCourse();
                    return new CourseDTO(
                            course.getCourseId(),
                            course.getCourseTitle(),
                            course.getCourseDescription(),
                            course.getCourseInstructor(),
                            course.getLevelId(),
                            course.getCourseImageCover());
                })
                .collect(java.util.stream.Collectors.toList());
    }
}