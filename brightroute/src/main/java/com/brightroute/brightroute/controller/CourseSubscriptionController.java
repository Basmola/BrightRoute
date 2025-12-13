package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.dto.CourseSubscriptionDTO; // New import
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

    // CORRECTION: Change return type to CourseSubscriptionDTO
    @PostMapping("/subscribe")
    public CourseSubscriptionDTO subscribe( 
            @RequestParam Integer userId,
            @RequestParam Integer courseId
    ) {
        // The service now returns the DTO directly
        return courseSubscriptionService.subscribe(userId, courseId);
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribe(
            @RequestParam Integer userId,
            @RequestParam Integer courseId
    ) {
        courseSubscriptionService.unsubscribe(userId, courseId);
    }
}