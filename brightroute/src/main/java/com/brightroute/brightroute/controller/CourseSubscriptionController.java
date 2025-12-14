package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.dto.CourseSubscriptionDTO;
import com.brightroute.brightroute.dto.CourseDTO;
import com.brightroute.brightroute.service.CourseSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course-subscription")
public class CourseSubscriptionController {

    private final CourseSubscriptionService courseSubscriptionService;

    @Autowired
    public CourseSubscriptionController(CourseSubscriptionService courseSubscriptionService) {
        this.courseSubscriptionService = courseSubscriptionService;
    }

    @PostMapping("/subscribe")
    public CourseSubscriptionDTO subscribe(
            @RequestParam Integer userId,
            @RequestParam Integer courseId) {
        return courseSubscriptionService.subscribe(userId, courseId);
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribe(
            @RequestParam Integer userId,
            @RequestParam Integer courseId) {
        courseSubscriptionService.unsubscribe(userId, courseId);
    }

    @GetMapping("/user/{userId}")
    public java.util.List<CourseDTO> getSubscribedCourses(@PathVariable Integer userId) {
        return courseSubscriptionService.getSubscribedCourses(userId);
    }
}