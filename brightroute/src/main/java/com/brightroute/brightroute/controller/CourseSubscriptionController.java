package com.brightroute.brightroute.controller;

import com.brightroute.brightroute.model.CourseSubscription;
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
    public CourseSubscription subscribe(
            // CORRECTION: Changed type to Integer, renamed parameter to userId
            @RequestParam Integer userId,
            @RequestParam Integer courseId
    ) {
        return courseSubscriptionService.subscribe(userId, courseId);
    }

    @DeleteMapping("/unsubscribe")
    public void unsubscribe(
            // CORRECTION: Changed type to Integer, renamed parameter to userId
            @RequestParam Integer userId,
            @RequestParam Integer courseId
    ) {
        courseSubscriptionService.unsubscribe(userId, courseId);
    }
}